package it.univr.pl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    /*
    private static final String IN1 =
    """
    int counter <- 0;
    int flag <- 0;
    repeat while (counter < 5) {
        counter++
    };
    if ('a') then {
        flag <- 1
    } else {
        flag <- 2
    };
    int input <- read int;
    int rand1 <- input @ int rand2 <- input
    """;

    private static final String IN9 =
    """
    int y[] <- {81,20};
    int x <- 10;
    int z <- 0;
    if(z == 0) then {
        x <- (x - 3)*2
    }
    """;

    private static final String IN3 =
    """
    int y[] <- {81,20};
    int x <- 0;
    int z <- 0;
    if(z == 0) then {
        x <- y[1]*3
    }                
    """;

    private static final String IN10 =
    """
    int x <- 0;
    switch (x) {
        default : {
            disp "ciao"
        }
    }
    """;
     */

    private static final String IN =
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
    }
    """;


    public static void main(String[] args) {
        //String prog = Program.programma;
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