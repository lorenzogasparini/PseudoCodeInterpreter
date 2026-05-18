import it.univr.pl.Mem;
import it.univr.pl.PseudoCodeIntp;
import it.univr.pl.PseudoCodeTS;
import it.univr.pl.exception.TypeMismatchException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PseudoCodeUTests {
    /**
     * Metodo di supporto: prende una stringa di codice, fa girare Lexer, Parser,
     * TypeSystem e Interprete, e restituisce la Memoria finale per le asserzioni.
     */
    private Mem runCode(String code) {
        CharStream cs = CharStreams.fromString(code);
        PseudoCodeLexer lexer = new PseudoCodeLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PseudoCodeParser parser = new PseudoCodeParser(tokens);
        ParseTree tree = parser.main();

        // Type Checking
        PseudoCodeTS typeSystem = new PseudoCodeTS();
        typeSystem.visit(tree);

        // Interpretazione
        PseudoCodeIntp interpreter = new PseudoCodeIntp();
        interpreter.visit(tree);

        return interpreter.getMem();
    }

    @Test
    public void testAritmeticaBase() {
        String code = """
                int x <- 10;
                int y <- 3;
                int z <- x + y * 2;
                dec w <- z / 2.0
                """; // Senza punto e virgola finale come deciso prima!

        Mem mem = runCode(code);

        // x = 10, y = 3, z = 10 + 6 = 16, w = 16 / 2.0 = 8.0
        assertEquals(16, mem.getValue("z").toJavaValue());
        assertEquals(8.0, mem.getValue("w").toJavaValue());
    }

    @Test
    public void testRequisitiAvanzati() {
        String code = """
                int a <- 5;
                a++;
                int b <- 10;
                b -= 3;
                string res <- (a == 6) ? "ok" : "no"
                """;

        Mem mem = runCode(code);

        assertEquals(6, mem.getValue("a").toJavaValue(), "L'operatore ++ ha fallito");
        assertEquals(7, mem.getValue("b").toJavaValue(), "L'operatore -= ha fallito");
        assertEquals("ok", mem.getValue("res").toJavaValue(), "L'operatore ternario ha fallito");
    }

    @Test
    public void testCicliECondizionali() {
        String code = """
                int counter <- 0;
                int flag <- 0;
                repeat while (counter < 5) {
                    counter++;
                }
                if (counter == 5) then {
                    flag <- 1;
                } else {
                    flag <- 2;
                }
                """;

        Mem mem = runCode(code);

        assertEquals(5, mem.getValue("counter").toJavaValue(), "Il ciclo repeat while non ha eseguito il numero corretto di iterazioni");
        assertEquals(1, mem.getValue("flag").toJavaValue(), "L'if-else non ha preso il ramo corretto");
    }

    @Test
    public void testTipiDiversi() {
        String code = """
                char c <- 'x';
                string s <- "ciao\\nmondo";
                bool t <- T;
                bool f <- NOT t OR (5 > 10)
                """;

        Mem mem = runCode(code);

        assertEquals('x', mem.getValue("c").toJavaValue());
        assertEquals("ciao\nmondo", mem.getValue("s").toJavaValue());
        assertEquals(true, mem.getValue("t").toJavaValue());
        assertEquals(false, mem.getValue("f").toJavaValue());
    }

    @Test
    public void testTypeSystemBloccaErroreDiTipo() {
        String code = """
                int a <- 5;
                a <- "testo"
                """; // Assegna una stringa a un intero

        // Ci aspettiamo che il TypeSystem si arrabbi e lanci la TypeMismatchException
        assertThrows(TypeMismatchException.class, () -> {
            runCode(code);
        }, "Il Type System non ha bloccato un'assegnazione errata!");
    }

    @Test
    public void testInterpreteBloccaDivisionePerZero() {
        String code = """
                int a <- 10;
                int b <- 0;
                int c <- a / b
                """;

        // Ci aspettiamo che l'Interprete si accorga della divisione per zero
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            runCode(code);
        });

        assertTrue(exception.getMessage().contains("Divisione per zero"));
    }
}
