package it.univr.pl;

public class Program {
    static final String programmaTest =
    """
    int x <- 5;
    int y <- 2;
    dec z <- 3.5;
    bool flag <- T;
    string msg <- "inizio";
    char c <- 'a';
    
    int arr[] <- {1,2,3,4,5};
    
    // assegnamenti e operatori
    x += 3;
    y--;
    z <- z ^ 2;
    
    // accesso array
    arr[2] <- x * y;
    
    // if-else con espressioni complesse
    if (x > y AND flag) then {
        msg <- "x maggiore di y"
    } else {
        msg <- "x minore o uguale a y"
    };
    
    // ternario
    string risultato <- (x > 5) ? "grande" : "piccolo";
    
    // while (repeat while)
    repeat while (x > 0) {
        x--;
        disp "loop ${x}"
    };
    
    // switch su interi
    switch (y) {
        case 1: {
            disp "uno"
        }
        case 2: {
            disp "due"
        }
        case 3: {
            disp "tre"
        }
        default: {
            disp "altro"
        }
    };
    
    // switch su stringhe
    switch (msg) {
        case "inizio": {
            disp "start"
        }
        case "x maggiore di y": {
            disp "ok"
        }
        default: {
            disp "nessuno"
        }
    };
    
    // non determinismo
    disp "ramo1" @ disp "ramo2" @ disp "ramo3";
    
    // sequenze
    disp msg;
    disp risultato;
    disp arr[2];
    
    // input
    int inputVal <- read int;
    
    // uso combinato
    if (inputVal > 10) then {
        disp "input grande"
    } else {
        disp "input piccolo"
    }
    """;
}