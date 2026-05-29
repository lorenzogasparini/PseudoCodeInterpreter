package it.univr.pl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    /* TEST SWITCH:

    switch(x){
        case 1 : {x <- 2}
        case 2 : {x <- x^2; y <- x+1}
        default : {x <- x+3}
    }

    */

    private static final String IN1 =
            """
            int counter <- 0;
            int flag <- 0;
            repeat while (counter < 5) {
                counter++
            };
            if (counter == 5) then {
                flag <- 1
            } else {
                flag <- 2
            };
            int input <- read int;
            int rand1 <- input @ int rand2 <- input
            """;

    /* Test che deve restituire dalla memoria una variabile di tipo int denominata x di valore 82:
    { x[int]:82 }
    Type checking superato con successo!
    */
    private static final String IN =
            """
            int x <- 1;
            switch(x){
                case 0:{
                    x <- 81;
                }
                case 1:{
                    x <- 82;
                }
                default:{
                    x <- 83
                }
            }
            """;

    /* Test che deve restituire dalla memoria una variabile di tipo int denominata x di valore 83:
    { x[int]:83 }
    Type checking superato con successo!
    */
    private static final String IN2 =
            """
            int x <- 5;
            switch(x){
                case 0:{
                    x <- 81;
                }
                case 1:{
                    x <- 82;
                }
                default:{
                    x <- 83
                }
            }
            """;

    public static void main(String[] args) {
        String prog = IN;
        CharStream cs = CharStreams.fromString(prog);
        PseudoCodeLexer lexer = new PseudoCodeLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PseudoCodeParser parser = new PseudoCodeParser(tokens);
        ParseTree tree = parser.main();
        PseudoCodeTS typeSystem = new PseudoCodeTS();
        try {
            typeSystem.visit(tree);
            PseudoCodeIntp interpreter = new PseudoCodeIntp();
            interpreter.visit(tree);
            System.out.println(interpreter.getMem());
            System.out.println("Type checking superato con successo!");
        } catch (RuntimeException re) {
            System.out.println("Typing error(s) found.");
            System.out.println(re.getMessage());
        }
    }
}