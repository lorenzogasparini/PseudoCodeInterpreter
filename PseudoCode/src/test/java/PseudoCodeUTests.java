import it.univr.pl.*;
import it.univr.pl.exception.TypeMismatchException;
import it.univr.pl.exception.VarDeclarationException;
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
                    counter++
                };
                if (counter == 5) then {
                    flag <- 1
                } else {
                    flag <- 2
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
    public void testTypeSystemBloccaIndiceVariabileErrato() {
        // Test per verificare che il TypeSystem blocchi una variabile non intera usata come indice
        String code = """
                int a[] <- {10, 20, 30};
                string s <- "ciao";
                a[s] <- 99
                """;

        assertThrows(TypeMismatchException.class, () -> {
            runCode(code);
        });
    }

    @Test
    public void testTypeSystemBloccaIndiceVariabileNonDichiarata() {
        // Test per verificare che il TypeSystem si accorga se la variabile indice non esiste
        String code = """
                int a[] <- {10, 20, 30};
                a[variabileInesistente] <- 99
                """;

        assertThrows(VarDeclarationException.class, () -> {
            runCode(code);
        });
    }

    @Test
    public void testInterpreteBloccaDivisionePerZero() {
        String code = """
                int a <- 10;
                int b <- 0;
                int c <- a / b;
                disp c
                """;

        // Ci aspettiamo che l'Interprete si accorga della divisione per zero
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            runCode(code);
        });

        assertTrue(exception.getMessage().contains("Divisione per zero"));
    }

    @Test
    public void testInterpreteCostruttoSwitchDefaultCase() {
        String code = """
                int x <- 5;
                switch(x){
                    case 0:{
                        x <- 81
                    }
                    case 1:{
                        x <- 82
                    }
                    default:{
                        x <- 83
                    }
                }
                """;
        Mem mem = runCode(code);

        assertEquals(83, mem.getValue("x").toJavaValue());
    }

    @Test
    public void testInterpreteCostruttoSwitchCase() {
        String code = """
            int x <- 1;
            switch(x){
                case 0:{
                    x <- 81
                }
                case 1:{
                    x <- 82
                }
                default:{
                    x <- 83
                }
            }
            """;
        Mem mem = runCode(code);

        assertEquals(82, mem.getValue("x").toJavaValue());
    }

    @Test
    public void testInterpeteCostruttoSwitchTipi() {
        String code = """
                char y <- 'a';
                int x <- 0;
                switch(y){
                    case 'a':{
                        x <- 81
                    }
                    case 'b':{
                        x <- 82
                    }
                    default:{
                        x <- 83
                    }
                }
                """;
        Mem mem = runCode(code);

        assertEquals(81, mem.getValue("x").toJavaValue());
    }

    @Test
    public void testInterpreteCostruttoSwitchTipoString() {
        String code = """
                string y <- "aasd";
                int x <- 0;
                switch(y){
                    case "asdsadad":{
                       x <- 81
                    }
                    case "aasd":{
                       x <- 82
                    }
                    default:{
                       x <- 83
                    }
                }
                """;
        Mem mem = runCode(code);

        assertEquals(82, mem.getValue("x").toJavaValue());
    }

    @Test
    public void testInterpreteCostruttoSwitchTipoErroneo() {
        String code = """
                int y <- 1;
                int x <- 0;
                switch(y){
                    case 1:{
                        x <- 81
                    }
                    case "asd":{
                        x <- 81
                    }
                    case 12:{
                        x <- 82
                    }
                    default:{
                        x <- 83
                    }
                }
                """;
        // Ci aspettiamo che l'Interprete si accorga del tipo erroneo inserito come case
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            runCode(code);
        });

        assertTrue(exception.getMessage().contains("Type mismatch in switch case"));
    }

    @Test
    public void testInterpreteCostruttoSwitchConCaseDuplicato() {
        String code = """
                string y <- "asd";
                int x <- 0;
                int z <- 0;
                switch(y){
                    case "asd":{
                        x <- 81;
                        y <- "asdasd"
                    }
                    case "asdasd":{
                        x <- 82;
                        y <- "asdasd"
                    }
                    case "asd":{
                        x <- 81;
                        y <- "asdasd"
                    }
                    default:{
                        z <- 3
                    }
                }
                """;
        // Ci aspettiamo che l'Interprete si accorga del valore di case duplicato
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            runCode(code);
        });

        assertTrue(exception.getMessage().contains("Case duplicate in switch case"));
    }

    @Test
    public void testInterpreteDichiarazioneArray() {
        String code = """
                int y[] <- {81,82};
                int x <- 0;
                int z <- 0
                """;
        Mem mem = runCode(code);

        assertEquals(81, mem.getValue("y_0").toJavaValue());
        assertEquals(82, mem.getValue("y_1").toJavaValue());
    }

    @Test
    public void testInterpreteAssegnamentoArray() {
        String code = """
                int y[] <- {81,82};
                int x <- 0;
                int z <- 0;
                if(x == 0) then {
                    y[1] <- 86
                }
                """;
        Mem mem = runCode(code);

        assertEquals(81, mem.getValue("y_0").toJavaValue());
        assertEquals(86, mem.getValue("y_1").toJavaValue());
    }

    @Test
    public void testInterpreteUtilizzoArrayVal() {
        String code = """
                int y[] <- {81,20};
                int x <- 0;
                int z <- 0;
                if(z == 0) then {
                    x <- y[1]*3
                }
                """;
        Mem mem = runCode(code);

        assertEquals(60, mem.getValue("x").toJavaValue());
    }

    @Test
    public void testAssignArrayIndiceVariabile() {
        String code = """
            int a[] <- {10,20,30};
            int i <- 1;
            a[i] <- 99
            """;

        Mem mem = runCode(code);

        assertEquals(10, mem.getValue("a_0").toJavaValue());
        assertEquals(99, mem.getValue("a_1").toJavaValue());
        assertEquals(30, mem.getValue("a_2").toJavaValue());
    }

    @Test
    public void testAssignArrayCompleto() {
        String code = """
            int a[] <- {1,2,3};
            a[] <- {7,8,9}
            """;

        Mem mem = runCode(code);

        assertEquals(7, mem.getValue("a_0").toJavaValue());
        assertEquals(8, mem.getValue("a_1").toJavaValue());
        assertEquals(9, mem.getValue("a_2").toJavaValue());
    }

    @Test
    public void testArrayDopoAssegnazione() {
        String code = """
            int a[] <- {2,4,6};
            int i <- 0;
            a[i] <- 10;
            int x <- a[0] + a[1]
            """;

        Mem mem = runCode(code);

        assertEquals(14, mem.getValue("x").toJavaValue()); // 10 + 4
    }

    @Test
    public void testIndiceFuoriBound() {
        String code = """
            int a[] <- {1,2};
            a[5] <- 10
            """;

        assertThrows(RuntimeException.class, () -> {
            runCode(code);
        });
    }

    @Test
    public void testIndiceFuoriBoundConId() {
        String code = """
            int a[] <- {1,2};
            int i <- 5;
            a[i] <- 10
            """;

        assertThrows(RuntimeException.class, () -> {
            runCode(code);
        });
    }

    @Test
    public void testInterpolazioneSemplice() {
        // Il caso d'uso descritto nei requisiti del progetto
        String code = """
                int x <- 3;
                int y <- 2;
                string s <- "Risultato: ${x + y}"
                """;

        Mem mem = runCode(code);

        assertEquals("Risultato: 5", mem.getValue("s").toJavaValue());
    }


    @Test
    public void testInterpolazioneConEspressioniComplesse() {
        // Test con operatori ternari e logici dentro la stringa
        String code = """
                int a <- 10;
                string s <- "Valore: ${ (a > 5) ? 100 : 0 }"
                """;

        Mem mem = runCode(code);

        assertEquals("Valore: 100", mem.getValue("s").toJavaValue());
    }

    @Test
    public void testTypeSystemBloccaErroreInInterpolazione() {
        // Test per verificare che il TypeSystem controlli anche il codice DENTRO la stringa
        String code = """
                string s <- "Risultato: ${variabileInesistente + 5}"
                """;

        // Ci aspettiamo che il Type System intercetti la variabile non dichiarata PRIMA dell'esecuzione
        assertThrows(VarDeclarationException.class, () -> {
            runCode(code);
        });
    }

    @Test
    public void testTypeSystemBloccaErroreDiTipoInInterpolazione() {
        String code = """
                string testo <- "ciao";
                string s <- "Risultato: ${testo - 5}"
                """;

        // Non si può sottrarre un numero a una stringa. Il Type System deve bloccarlo.
        assertThrows(TypeMismatchException.class, () -> {
            runCode(code);
        });
    }
}
