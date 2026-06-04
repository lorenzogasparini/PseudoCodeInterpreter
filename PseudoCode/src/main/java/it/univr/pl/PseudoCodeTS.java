package it.univr.pl;

import it.univr.pl.exception.SwitchCaseException;
import it.univr.pl.exception.TypeMismatchException;
import it.univr.pl.exception.VarDeclarationException;
import it.univr.pl.type.*;
import it.univr.pl.value.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PseudoCodeTS extends PseudoCodeBaseVisitor<Type> {

    private final Map<String, ExpType> typeMap;

    public PseudoCodeTS(Map<String, ExpType> typeMap) {
        this.typeMap = new HashMap<>(typeMap);
    }

    public PseudoCodeTS() {
        typeMap = new HashMap<>();
    }

    public Map<String, ExpType> getTypeMap() {
        return typeMap;
    }

    private ComType visitCom(PseudoCodeParser.ComContext ctx) {
        return (ComType) visit(ctx);
    }

    private SimpleType visitBoolExp(PseudoCodeParser.ExpContext ctx) {
        ExpType expType = (ExpType) visit(ctx);
        if (!expType.isCompatible(SimpleType.BOOL)) {
            throw new TypeMismatchException("Type mismatch: boolean expression expected.\n@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
        }
        return SimpleType.BOOL;
    }

    private SimpleType visitNumExp(PseudoCodeParser.ExpContext ctx) {
        ExpType expType = (ExpType) visit(ctx);
        if (!expType.isCompatible(SimpleType.INT) && !expType.isCompatible(SimpleType.DEC)) {
            throw new TypeMismatchException("Type mismatch: numeric expression expected.\n@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
        }
        return (SimpleType) expType;
    }

    private boolean tryToUpcast(ExpType typeA, ExpType typeB) {
        return typeA == typeB || typeA.isCompatible(typeB) || typeB.isCompatible(typeA);
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
    public ComType visitMain(PseudoCodeParser.MainContext ctx) {
        return visitCom(ctx.com());
    }

    @Override
    public ComType visitDecl(PseudoCodeParser.DeclContext ctx) {
        ExpType varType = TypeUtils.fromString(ctx.TYPE().getText());
        String id = ctx.ID().getText();

        if (typeMap.containsKey(id)) {
            throw new VarDeclarationException("Variable " + id + " already declared.\n@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
        }
        typeMap.put(id, varType);

        if (ctx.exp() != null) {
            ExpType expType = (ExpType) visit(ctx.exp());
            if (!varType.isCompatible(expType)) {
                throw new TypeMismatchException("Variable " + id + " cannot be assigned with " + expType.getName() + ".\n@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
            }
        }
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitAssign(PseudoCodeParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        if (!typeMap.containsKey(id)) {
            throw new VarDeclarationException("Variable " + id + " assigned but never declared.\n@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
        }

        ExpType varType = typeMap.get(id);
        ExpType expType = (ExpType) visit(ctx.exp());
        if (!varType.isCompatible(expType)) {
            throw new TypeMismatchException("Variable " + id + " cannot be assigned with " + expType.getName() + ".\n@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
        }
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitAssignOp(PseudoCodeParser.AssignOpContext ctx) {
        String id = ctx.ID().getText();
        if (!typeMap.containsKey(id)) {
            throw new VarDeclarationException("Variable " + id + " assigned but never declared.\n@" + ctx.start.getLine());
        }
        ExpType varType = typeMap.get(id);
        if (!varType.isCompatible(SimpleType.INT) && !varType.isCompatible(SimpleType.DEC)) {
            throw new TypeMismatchException("Numeric variable expected for += or -=.\n@" + ctx.start.getLine());
        }
        visitNumExp(ctx.exp());
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitIncDec(PseudoCodeParser.IncDecContext ctx) {
        String id = ctx.ID().getText();
        if (!typeMap.containsKey(id)) {
            throw new VarDeclarationException("Variable " + id + " assigned but never declared.\n@" + ctx.start.getLine());
        }
        ExpType varType = typeMap.get(id);
        if (!varType.isCompatible(SimpleType.INT) && !varType.isCompatible(SimpleType.DEC)) {
            throw new TypeMismatchException("Numeric variable expected for ++ or --.\n@" + ctx.start.getLine());
        }
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitIfElse(PseudoCodeParser.IfElseContext ctx) {
        visitBoolExp(ctx.exp());

        PseudoCodeTS typeSystemIf = new PseudoCodeTS(typeMap);
        typeSystemIf.visitCom(ctx.com(0));

        if (ctx.com().size() > 1) { // Ramo else opzionale
            PseudoCodeTS typeSystemElse = new PseudoCodeTS(typeMap);
            typeSystemElse.visitCom(ctx.com(1));
        }
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitSwitch(PseudoCodeParser.SwitchContext ctx) {
        int i = 0;
        List<ExpValue<?>> caseNodeList = new ArrayList<>();

        for(TerminalNode caseNode : ctx.CASE()) {
            if(!caseNodeList.contains(getCaseValue(ctx, i))){
                // 'caseNode' rappresenta il token "case"
                ExpValue<?> caseValue = getCaseValue(ctx, i);
                caseNodeList.add(caseValue);

                ExpType expType = (ExpType) visit(ctx.exp());
                ExpType caseval = TypeUtils.fromValue(caseValue);

                if(expType.isCompatible(caseval)){
                    PseudoCodeTS typeSystemSwitch = new PseudoCodeTS(typeMap);
                    typeSystemSwitch.visitCom(ctx.com(i));
                } else{
                    //  TODO: capire come predisporre al meglio il numero della riga di errore nella stampa
                    throw new TypeMismatchException("Type mismatch in switch case.\n@" + ctx.start.getLine() + "-" + ctx.com().indexOf(ctx.com(i)));
                }
            }
            else{
                throw new SwitchCaseException("Case duplicate in switch case.\n@" + ctx.start.getLine());
            }
            i++;
        }
        if(ctx.DEFAULT() != null) {
            int defaultIndex = ctx.com().size()-1;
            if (defaultIndex >= 0 && ctx.com(defaultIndex) != null) {
                PseudoCodeTS typeSystemElse = new PseudoCodeTS(typeMap);
                typeSystemElse.visitCom(ctx.com(defaultIndex));
            }
        }

        return ComType.INSTANCE;
    }

    @Override
    public ComType visitWhile(PseudoCodeParser.WhileContext ctx) {
        visitBoolExp(ctx.exp());
        PseudoCodeTS typeSystem = new PseudoCodeTS(typeMap);
        return typeSystem.visitCom(ctx.com());
    }

    @Override
    public ComType visitNonDet(PseudoCodeParser.NonDetContext ctx) {
        // Controlla che tutti i rami della scelta siano comandi validi
        for (PseudoCodeParser.ComContext comCtx : ctx.com()) {
            visitCom(comCtx);
        }
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitSeq(PseudoCodeParser.SeqContext ctx) {
        visitCom(ctx.com(0));
        return visitCom(ctx.com(1));
    }

    @Override
    public ComType visitOut(PseudoCodeParser.OutContext ctx) {
        visit(ctx.exp()); // Accetta la stampa di qualsiasi espressione
        return ComType.INSTANCE;
    }

    @Override
    public ExpType visitId(PseudoCodeParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if (!typeMap.containsKey(id)) {
            throw new VarDeclarationException("Variable " + id + " not declared.\n@" + ctx.start.getLine());
        }
        return typeMap.get(id);
    }


    @Override
    public SimpleType visitMulDivMod(PseudoCodeParser.MulDivModContext ctx) {
        SimpleType left = visitNumExp(ctx.exp(0));
        SimpleType right = visitNumExp(ctx.exp(1));

        if (!tryToUpcast(left, right)) {
            throw new TypeMismatchException("Type mismatch in operation.\n@" + ctx.start.getLine());
        }
        return left.isCompatible(right) ? right : left;
    }

    @Override
    public SimpleType visitAddSub(PseudoCodeParser.AddSubContext ctx) {
        SimpleType left = visitNumExp(ctx.exp(0));
        SimpleType right = visitNumExp(ctx.exp(1));

        if (!tryToUpcast(left, right)) {
            throw new TypeMismatchException("Type mismatch in operation.\n@" + ctx.start.getLine());
        }
        return left.isCompatible(right) ? right : left;
    }

    @Override
    public SimpleType visitCmpExp(PseudoCodeParser.CmpExpContext ctx) {
        SimpleType left = visitNumExp(ctx.exp(0));
        SimpleType right = visitNumExp(ctx.exp(1));

        if (!tryToUpcast(left, right)) {
            throw new TypeMismatchException("Type mismatch in comparison.\n@" + ctx.start.getLine());
        }
        return SimpleType.BOOL;
    }

    @Override
    public SimpleType visitEqExp(PseudoCodeParser.EqExpContext ctx) {
        ExpType left = (ExpType) visit(ctx.exp(0));
        ExpType right = (ExpType) visit(ctx.exp(1));

        if (!tryToUpcast(left, right)) {
            throw new TypeMismatchException("Type mismatch in equality.\n@" + ctx.start.getLine());
        }
        return SimpleType.BOOL;
    }

    @Override
    public SimpleType visitAndOr(PseudoCodeParser.AndOrContext ctx) {
        visitBoolExp(ctx.exp(0));
        visitBoolExp(ctx.exp(1));
        return SimpleType.BOOL;
    }

    @Override
    public SimpleType visitNot(PseudoCodeParser.NotContext ctx) {
        visitBoolExp(ctx.exp());
        return SimpleType.BOOL;
    }

    @Override
    public SimpleType visitPow(PseudoCodeParser.PowContext ctx) {
        SimpleType base = visitNumExp(ctx.exp(0));
        SimpleType exponent = visitNumExp(ctx.exp(1));

        if (!tryToUpcast(base, exponent)) {
            throw new TypeMismatchException("Type mismatch in pow.\n@" + ctx.start.getLine());
        }
        return base.isCompatible(exponent) ? exponent : base;
    }

    @Override
    public ExpType visitParExp(PseudoCodeParser.ParExpContext ctx) {
        return (ExpType) visit(ctx.exp());
    }

    @Override
    public SimpleType visitTernary(PseudoCodeParser.TernaryContext ctx) {
        visitBoolExp(ctx.exp(0));
        ExpType left = (ExpType) visit(ctx.exp(1));
        ExpType right = (ExpType) visit(ctx.exp(2));

        if (!tryToUpcast(left, right)) {
            throw new TypeMismatchException("Ternary branches must have compatible types.\n@" + ctx.start.getLine());
        }
        return left.isCompatible(right) ? (SimpleType) right : (SimpleType) left;
    }

    @Override
    public SimpleType visitReadExp(PseudoCodeParser.ReadExpContext ctx) {
        // Ritorna esattamente il tipo richiesto dopo la parola "read" (es. int, string)
        return (SimpleType) TypeUtils.fromString(ctx.TYPE().getText());
    }

    @Override
    public SimpleType visitNumeric(PseudoCodeParser.NumericContext ctx) {
        return (SimpleType) visit(ctx.num());
    }

    @Override
    public SimpleType visitIntNum(PseudoCodeParser.IntNumContext ctx) { return SimpleType.INT; }

    @Override
    public SimpleType visitDecNum(PseudoCodeParser.DecNumContext ctx) { return SimpleType.DEC; }

    @Override
    public SimpleType visitBoolean(PseudoCodeParser.BooleanContext ctx) { return SimpleType.BOOL; }

    @Override
    public SimpleType visitString(PseudoCodeParser.StringContext ctx) { return SimpleType.STRING; }

    @Override
    public SimpleType visitChar(PseudoCodeParser.CharContext ctx) { return SimpleType.CHAR; }
}