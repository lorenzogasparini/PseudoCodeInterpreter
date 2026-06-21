package it.univr.pl;

import it.univr.pl.exception.TypeMismatchException;
import it.univr.pl.type.ComType;
import it.univr.pl.type.ExpType;
import it.univr.pl.type.SimpleType;
import it.univr.pl.type.TypeUtils;
import it.univr.pl.value.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class PseudoCodeIntp extends PseudoCodeBaseVisitor<Value> {

    private final Mem mem;

    public PseudoCodeIntp(Mem mem) {
        // Scoping: crea una nuova memoria isolata basata sulla precedente
        this.mem = new Mem(mem);
    }

    public PseudoCodeIntp() {
        mem = new Mem();
    }

    public Mem getMem() {
        return mem;
    }

    // metodi helper
    private ComValue visitCom(PseudoCodeParser.ComContext ctx) {
        return (ComValue) visit(ctx);
    }

    private ExpValue<?> visitExp(PseudoCodeParser.ExpContext ctx) {
        return (ExpValue<?>) visit(ctx);
    }

    private NumValue<?> visitNumExp(PseudoCodeParser.ExpContext ctx) {
        return (NumValue<?>) visitExp(ctx);
    }

    private BoolValue visitBoolExp(PseudoCodeParser.ExpContext ctx) {
        return (BoolValue) visitExp(ctx);
    }

    private StringValue visitStringExp(PseudoCodeParser.ExpContext ctx) {
        return (StringValue) visitExp(ctx);
    }

    private double unwrapToDouble(NumValue<?> numValue) {
        return numValue.toJavaValue().doubleValue();
    }

    @Override
    public ComValue visitMain(PseudoCodeParser.MainContext ctx) {
        return visitCom(ctx.com());
    }

    @Override
    public ComValue visitDecl(PseudoCodeParser.DeclContext ctx) {
        ExpType type = TypeUtils.fromString(ctx.TYPE().getText());
        String id = ctx.ID().getText();

        mem.add(id, type); // Alloca la variabile in memoria

        if (ctx.exp() != null) {
            ExpValue<?> val = visitExp(ctx.exp());
            mem.updateValue(id, val);
        }
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitDeclArray(PseudoCodeParser.DeclArrayContext ctx) {
        ExpType type = TypeUtils.fromString(ctx.TYPE().getText());
        String id = ctx.ID().getText();

        if (ctx.ASSIGN() != null) {
            int i = 0;
            for(PseudoCodeParser.ExpContext ex : ctx.exp()) {
                ExpValue<?> val = visitExp(ex);
                mem.add(id + "_" + i, val);
                i++;
            }
        }

        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitAssignArray(PseudoCodeParser.AssignArrayContext ctx) {
        String id = ctx.ID(0).getText();

        // Indice variabile (es. a[i] <- 5)
        if (ctx.ID(1) != null) {
            String indexVar = ctx.ID(1).getText();
            int index = (int) mem.getValue(indexVar).toJavaValue();
            ExpValue<?> val = visitExp(ctx.exp(0));
            mem.updateValue(id + "_" + index, val);
        }
        // Indice costante (es. a[2] <- 5)
        else if (ctx.INT() != null) {
            int index = Integer.parseInt(ctx.INT().getText());
            ExpValue<?> val = visitExp(ctx.exp(0));
            mem.updateValue(id + "_" + index, val);
        }
        // Assegnazione intero array (es. a[] <- {1, 2, 3})
        else {
            int i = 0;
            for(PseudoCodeParser.ExpContext ex : ctx.exp()) {
                ExpValue<?> val = visitExp(ex);
                String arrayKey = id + "_" + i;

                if(!mem.contains(arrayKey)) {
                    mem.add(arrayKey, val); // aumento dimensione array (dinamico)
                } else {
                    mem.updateValue(arrayKey, val);
                }
                i++;
            }
        }

        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitAssign(PseudoCodeParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        ExpValue<?> v = visitExp(ctx.exp());
        mem.updateValue(id, v); // Sicuro grazie al Type Checking preventivo
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitAssignOp(PseudoCodeParser.AssignOpContext ctx) {
        // Avanzato 1: +=, -=
        String id = ctx.ID().getText();
        NumValue<?> currentVal = (NumValue<?>) mem.getValue(id);
        NumValue<?> rightVal = visitNumExp(ctx.exp());

        double result = ctx.op.getType() == PseudoCodeParser.PLUS_ASSIGN
                ? unwrapToDouble(currentVal) + unwrapToDouble(rightVal)
                : unwrapToDouble(currentVal) - unwrapToDouble(rightVal);

        // Mantiene il tipo originale se entrambi sono interi
        if (currentVal instanceof IntValue && rightVal instanceof IntValue) {
            mem.updateValue(id, new IntValue((int) result));
        } else {
            mem.updateValue(id, new DecValue(result));
        }
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitIncDec(PseudoCodeParser.IncDecContext ctx) {
        // Avanzato 1: ++, --
        String id = ctx.ID().getText();
        NumValue<?> currentVal = (NumValue<?>) mem.getValue(id);

        double result = ctx.op.getType() == PseudoCodeParser.INC
                ? unwrapToDouble(currentVal) + 1
                : unwrapToDouble(currentVal) - 1;

        if (currentVal instanceof IntValue) {
            mem.updateValue(id, new IntValue((int) result));
        } else {
            mem.updateValue(id, new DecValue(result));
        }
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitIfElse(PseudoCodeParser.IfElseContext ctx) {
        PseudoCodeIntp interpreter = new PseudoCodeIntp(mem); // Crea nuovo scope

        if (visitBoolExp(ctx.exp()).toJavaValue()) {
            interpreter.visitCom(ctx.com(0));
        } else if (ctx.com().size() > 1) { // Ramo else opzionale
            interpreter.visitCom(ctx.com(1));
        }

        mem.updateValues(interpreter.getMem()); // Sincronizza solo le variabili preesistenti
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitSwitch(PseudoCodeParser.SwitchContext ctx) {
        PseudoCodeIntp interpreter = new PseudoCodeIntp(mem); // Crea nuovo scope
        int i = 0;
        boolean flag = false;

        ExpValue<?> switchExp = visitExp(ctx.exp());

        for(TerminalNode caseNode : ctx.CASE()) {
            // 'caseNode' rappresenta il token "case"
            ExpValue<?> caseValue = getCaseValue(ctx, i);

            if (switchExp != null && switchExp.equals(caseValue)) {
                flag = true;

                if (ctx.com(i) != null) {
                    interpreter.visitCom(ctx.com(i));
                }
                break;
                /*
                Se si verifica la condizione del case terminiamo l'esecuzione
                (comportamento senza fall-through)
                */
            }

            i++;
        }

        if(!flag && ctx.DEFAULT() != null) {
            //  Dal momento che il comando di default se esiste si trova per ultimo (vedi grammatica: ...
            //  (DEFAULT ':' LBRACE com RBRACE)? RBRACE ...) sappiamo di doverlo andare a cercare in ctx.com().size()-1
            int defaultIndex = ctx.com().size()-1;
            if (defaultIndex >= 0 && ctx.com(defaultIndex) != null) {
                interpreter.visitCom(ctx.com(defaultIndex));
            }
        }

        mem.updateValues(interpreter.getMem()); // Sincronizza solo le variabili preesistenti
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitWhile(PseudoCodeParser.WhileContext ctx) {
        if (!visitBoolExp(ctx.exp()).toJavaValue()) {
            return ComValue.INSTANCE;
        }

        PseudoCodeIntp interpreter = new PseudoCodeIntp(mem);
        interpreter.visitCom(ctx.com());
        mem.updateValues(interpreter.getMem());

        return visitWhile(ctx); // Ricorsione per il ciclo
    }

    @Override
    public ComValue visitNonDet(PseudoCodeParser.NonDetContext ctx) {
        int rndIdx = new Random().nextInt(ctx.com().size());
        new PseudoCodeIntp(mem).visitCom(ctx.com(rndIdx));
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitSeq(PseudoCodeParser.SeqContext ctx) {
        visitCom(ctx.com(0));
        return visitCom(ctx.com(1));
    }

    @Override
    public ComValue visitOut(PseudoCodeParser.OutContext ctx) {
        // Stampa a video il valore effettivo dell'espressione
        System.out.println(visitExp(ctx.exp()).toJavaValue());
        return ComValue.INSTANCE;
    }


    @Override
    public ExpValue<?> visitMulDivMod(PseudoCodeParser.MulDivModContext ctx) {
        NumValue<?> left = visitNumExp(ctx.exp(0));
        NumValue<?> right = visitNumExp(ctx.exp(1));

        double leftVal = unwrapToDouble(left);
        double rightVal = unwrapToDouble(right);

        if ((ctx.op.getType() == PseudoCodeParser.DIV || ctx.op.getType() == PseudoCodeParser.MOD) && rightVal == 0) {
            throw new RuntimeException("Divisione per zero a tempo di esecuzione!");
        }

        Double result = switch (ctx.op.getType()) {
            case PseudoCodeParser.DIV -> leftVal / rightVal;
            case PseudoCodeParser.MUL -> leftVal * rightVal;
            case PseudoCodeParser.MOD -> leftVal % rightVal;
            default -> null; // irraggiungibile
        };

        if (left instanceof IntValue && right instanceof IntValue) {
            return new IntValue(result.intValue());
        } else {
            return new DecValue(result);
        }
    }

    @Override
    public ExpValue<?> visitAddSub(PseudoCodeParser.AddSubContext ctx) {
        NumValue<?> left = visitNumExp(ctx.exp(0));
        NumValue<?> right = visitNumExp(ctx.exp(1));

        Double result = switch (ctx.op.getType()) {
            case PseudoCodeParser.ADD -> unwrapToDouble(left) + unwrapToDouble(right);
            case PseudoCodeParser.SUB -> unwrapToDouble(left) - unwrapToDouble(right);
            default -> null;
        };

        if (left instanceof IntValue && right instanceof IntValue) {
            return new IntValue(result.intValue());
        } else {
            return new DecValue(result);
        }
    }

    @Override
    public BoolValue visitEqExp(PseudoCodeParser.EqExpContext ctx) {
        ExpValue<?> left = visitExp(ctx.exp(0));
        ExpValue<?> right = visitExp(ctx.exp(1));

        return switch (ctx.op.getType()) {
            case PseudoCodeParser.EQQ -> new BoolValue(left.equals(right));
            case PseudoCodeParser.NEQ -> new BoolValue(!left.equals(right));
            default -> null;
        };
    }

    @Override
    public BoolValue visitCmpExp(PseudoCodeParser.CmpExpContext ctx) {
        NumValue<?> left = visitNumExp(ctx.exp(0));
        NumValue<?> right = visitNumExp(ctx.exp(1));

        return switch (ctx.op.getType()) {
            case PseudoCodeParser.GEQ -> new BoolValue(unwrapToDouble(left) >= unwrapToDouble(right));
            case PseudoCodeParser.LEQ -> new BoolValue(unwrapToDouble(left) <= unwrapToDouble(right));
            case PseudoCodeParser.LT -> new BoolValue(unwrapToDouble(left) < unwrapToDouble(right));
            case PseudoCodeParser.GT -> new BoolValue(unwrapToDouble(left) > unwrapToDouble(right));
            default -> null;
        };
    }

    @Override
    public BoolValue visitAndOr(PseudoCodeParser.AndOrContext ctx) {
        BoolValue left = visitBoolExp(ctx.exp(0));
        BoolValue right = visitBoolExp(ctx.exp(1));

        return switch (ctx.op.getType()) {
            case PseudoCodeParser.AND -> new BoolValue(left.toJavaValue() && right.toJavaValue());
            case PseudoCodeParser.OR -> new BoolValue(left.toJavaValue() || right.toJavaValue());
            default -> null;
        };
    }

    @Override
    public BoolValue visitNot(PseudoCodeParser.NotContext ctx) {
        return new BoolValue(!visitBoolExp(ctx.exp()).toJavaValue());
    }

    @Override
    public ExpValue<?> visitPow(PseudoCodeParser.PowContext ctx) {
        NumValue<?> base = visitNumExp(ctx.exp(0));
        NumValue<?> exponent = visitNumExp(ctx.exp(1));

        double result = Math.pow(unwrapToDouble(base), unwrapToDouble(exponent));

        if (base instanceof IntValue && exponent instanceof IntValue) {
            return new IntValue((int) result);
        } else {
            return new DecValue(result);
        }
    }

    @Override
    public ExpValue<?> visitParExp(PseudoCodeParser.ParExpContext ctx) {
        return visitExp(ctx.exp());
    }

    @Override
    public ExpValue<?> visitTernary(PseudoCodeParser.TernaryContext ctx) {
        // Avanzato 1: Operatore Ternario
        boolean condition = visitBoolExp(ctx.exp(0)).toJavaValue();
        return condition ? visitExp(ctx.exp(1)) : visitExp(ctx.exp(2));
    }

    @Override
    public ExpValue<?> visitReadExp (PseudoCodeParser.ReadExpContext ctx) {
        Scanner scanner = new Scanner(System.in);
        SimpleType inputType = SimpleType.valueOf(ctx.TYPE().getText().toUpperCase());

        return switch (inputType) {
            case BOOL -> {
                System.out.println("read [BOOL]: ");
                yield new BoolValue(scanner.nextBoolean());
            }
            case INT -> {
                System.out.println("read [INT]: ");
                yield new IntValue(scanner.nextInt());
            }
            case DEC -> {
                System.out.println("read [DEC]: ");
                yield new DecValue(scanner.nextDouble());
            }
            case CHAR -> {
                System.out.println("read [CHAR]: ");
                yield new CharValue(scanner.next().charAt(0));
            }
            case STRING -> {
                System.out.println("read [STRING]: ");
                yield new StringValue(scanner.next());
            }
            default -> throw new TypeMismatchException("");
        };

    }

    @Override
    public ExpValue<?> visitId(PseudoCodeParser.IdContext ctx) {
        return mem.getValue(ctx.ID().getText());
    }

    private ExpValue<?> getCaseValue(PseudoCodeParser.SwitchContext ctx, int index) {
        Token t = ctx.op.get(index);
        String text = t.getText();
        switch (t.getType()) {
            case PseudoCodeParser.INT:
                return new IntValue(Integer.parseInt(text));
            case PseudoCodeParser.STRING:
                return new StringValue(StringEscapeUtils.unescapeJava(text.substring(1, text.length() - 1)));
            case PseudoCodeParser.BOOL:
                return new BoolValue(Boolean.parseBoolean(text));
            case PseudoCodeParser.DEC:
                return new DecValue(Double.parseDouble(text));
            case PseudoCodeParser.CHAR:
                return new CharValue(text.charAt(1));
            default:
                return null;
        }
    }

    @Override
    public ExpValue<?> visitArrayVal(PseudoCodeParser.ArrayValContext ctx) {
        String id = ctx.ID().getText();
        int index = (int) visitNumExp(ctx.exp()).toJavaValue();
        String key = id + "_" + index;

        if (!mem.contains(key)) {
            throw new RuntimeException("Array index out of bounds: " + index);
        }

        return mem.getValue(key);
    }

    @Override
    public NumValue<?> visitNumeric(PseudoCodeParser.NumericContext ctx) {
        return (NumValue<?>) visit(ctx.num());
    }

    @Override
    public IntValue visitIntNum(PseudoCodeParser.IntNumContext ctx) {
        return new IntValue(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public DecValue visitDecNum(PseudoCodeParser.DecNumContext ctx) {
        return new DecValue(Double.parseDouble(ctx.DEC().getText()));
    }

    @Override
    public BoolValue visitBoolean(PseudoCodeParser.BooleanContext ctx) {
        return new BoolValue(ctx.BOOL().getText().equals("T"));
    }

    @Override
    public StringValue visitString(PseudoCodeParser.StringContext ctx) {
        String raw = ctx.STRING().getText();
        String str = raw.substring(1, raw.length() - 1);
        str = StringEscapeUtils.unescapeJava(str);

        // Regex per ${...}
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\$\\{([^}]+)}");
        java.util.regex.Matcher matcher = pattern.matcher(str);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String exprText = matcher.group(1);

            // PARSING DELL'ESP
            PseudoCodeLexer lexer = new PseudoCodeLexer(CharStreams.fromString(exprText));
            PseudoCodeParser parser = new PseudoCodeParser(new CommonTokenStream(lexer));

            ExpValue<?> value = visitExp(parser.exp());

            matcher.appendReplacement(result, value.toJavaValue().toString()); // result ha il testo normale e value viene sostituito a ${...}
        }

        matcher.appendTail(result);

        return new StringValue(result.toString());
    }

    @Override
    public CharValue visitChar(PseudoCodeParser.CharContext ctx) {
        // Prende il carattere esatto escludendo i singoli apici
        char c = ctx.CHAR().getText().charAt(1);
        return new CharValue(c);
    }
}