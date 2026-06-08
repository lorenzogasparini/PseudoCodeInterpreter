<div align="center">
  <h1 align="center">Relazione <em>PseudoCodeInterpreter</em></h1>
  <p align="center">
    Elaborato conclusivo del corso <em>Linguaggi</em>: Implementazione di un linguaggio general purpose sulla base dei vincoli forniti dalla specifica.
    <br><br>
  </p>
</div>

> _PsuedoCodeInterpreter_ è un linguaggio general purpose che mira a realizzare dal punto di visto della computazione quanto generalmente inteso come pseudocodice.

## Indice

1. [Introduzione](#1-Introduzione)
2. [Guida rapida](#2-Guida-rapida)
3. [Sintassi](#3-Sintassi)
4. [Semantica](#4-Semantica)
5. [Implementazione](#5-Implementazione)
6. [Programmi di Test](#6-Programmi-di-test)

## 1. Introduzione

**PseudoCode** è un linguaggio di programmazione imperativo tipizzato sviluppato come linguaggio general purpose per la definizione di programmi che implementino algoritmi fondati sullo pseudo-codice.

**PseudoCodeInterpreter** è lo specifico interprete che si è implementato al fine di realizzare lo strumento atto al riconoscimento della sintassi, nonchè la definizione della semantica statica e l'assegnamento della semantica dinamica del linguaggio stesso.

### Caratteristiche principali

- **Tipizzazione statica** con tipi di dominio general purpose per i linguaggi di programmazione (`int`, `dec`, `bool`, `char`, `string`).
- **Espressioni aritmetico-logiche** complete con precedenza degli operatori.
- **Costrutti iterativi e condizionali di base**: `while`, `for`, `if`-`else`.
- **Arrays** come struttura dati di base.
- **Switch/case** come costrutto di selezione aggiuntivo.
- **Non determinismo** atto al supporto per un costrutto di scelta non deterministica, che esegue
  un comando tratto da una lista con una probabilità proporzionale alla dimensione
  della lista.
- **Zucchero sintattico**: assegnamenti composti (`+=`, `-=`, `*=`, `/=`), incremento e decremento unitario (`++`, `--`), **?????operatore ternario (`_ ? _ : _`)???????**, e interpolazione di espressioni nelle stringhe (`i"...${expr}..."`).
- **Gestione degli errori**: divisione per zero, accesso fuori dai limiti di un array, variabile non dichiarata, errori di tipo (tutti gestiti con messaggi esplicativi), errori nell'utilizzo scorretto del costrutto switch/case.

## 2. Guida rapida
### Requisiti

- **Java 11** o superiore, necessario per ANTLR
- **ANTLR 4** (versione 4.13.2 o superiore)
- **JUnit** (versione 5.10.0 o superiore), per l'esecuzione dei test inclusi in `PseudoCodeInterpreter/PseudoCode/src/test`

### Esecuzione programmi sample
#### Dichiarazione di variabili e assegnamento di valori
Dato il programma di esempio che ben rappresenta la _dichiarazione di variabili e assegnamento di valori_
```
int x <- 10;
int y <- 3;
int z <- x + y * 2;
dec w <- z / 2.0
```
otterremo i risultati:
>
> { w[dec]:8.0 x[int]:10 y[int]:3 z[int]:16 }
> 
> Type checking superato con successo!
>

#### Test costrutti iterativi e selezione
Dato il programma di esempio che ben rappresenta i _costrutti iterativi e selezione_
```
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
```
otterremo i risultati:
>
>{ flag[int]:1 counter[int]:5 }
> 
> Type checking superato con successo!
>

#### Test requisiti avanzati costrutto selezione
Dato il programma di esempio che ben rappresenta i _requisiti avanzati per il costrutto selezione_
```
int a <- 5;
a++;
int b <- 10;
b -= 3;
string res <- (a == 6) ? "ok" : "no"
```
otterremo i risultati:
>
> { a[int]:6 res[string]:ok b[int]:7 }
> 
> Type checking superato con successo!
>

#### Test gestione errore dato da operazione su variabili di tipo diverso
Dato il programma di esempio che ben rappresenta la _gestione errore dato da operazione su variabili di tipo diverso_
```
public void testTipiDiversi() {
String code = """
char c <- 'x';
string s <- "ciao\\nmondo";
bool t <- T;
bool f <- NOT t OR (5 > 10)
```
otterremo i risultati:
>
> line 1:15 no viable alternative at input 'publicvoid'
> 
> line 2:20 token recognition error at: '= '
> 
> line 2:24 token recognition error at: '"\n'
> 
> { }
> 
> Type checking superato con successo!

#### Test gestione errore dato da operazione su variabili di tipo diverso
Dato il programma di esempio che ben rappresenta la _gestione errore dato da operazione su variabili di tipo diverso_
```
char c <- 'x';
string s <- "ciao\\nmondo";
bool t <- T;
bool f <- NOT t OR (5 > 10)
```
otterremo i risultati:
>
> { c[char]:x s[string]:ciao\nmondo t[bool]:true f[bool]:false }
> 
> Type checking superato con successo!
>

#### Test gestione errore di tipo da parte del type system
Dato il programma di esempio che ben rappresenta la _gestione errore di tipo da parte del type system_
```
int a <- 5;
a <- "testo"
```
otterremo i risultati:
>
> Typing error(s) found.
> 
> Variable a cannot be assigned with string.
> 
> @2:16
>

#### Test gestione errore di tipo da parte del type system
Dato il programma di esempio che ben rappresenta la _gestione errore di tipo da parte del type system_
```
int a <- 10;
int b <- 0;
int c <- a / b;
disp c
```
otterremo i risultati:
>
> Typing error(s) found.
>
> Divisione per zero a tempo di esecuzione!
>

#### Test costrutto SwitchCase
Dato il programma di esempio che ben rappresenta l'utilizzo del costrutto _SwitchCase_
```
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
```
otterremo i risultati:
>
> { x[int]:83 }
> 
> Type checking superato con successo!
>

#### Test costrutto SwitchCase con tipo variabile erroneo per case
Dato il programma di esempio che ben rappresenta l'utilizzo del costrutto _SwitchCase_ per la quale dobbiamo ottenere un errore dato da tipo erroneo per l'espressione di riferimento per la selezione:
```
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
```
otterremo i risultati:
>
> Typing error(s) found.
> 
> Type mismatch in switch case.
> 
> @3-1
>

#### Test costrutto SwitchCase con duplicati nei case
Dato il programma di esempio che ben rappresenta l'utilizzo del costrutto _SwitchCase_ per la quale dobbiamo ottenere un errore dato da un duplicato per i case:
```
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
```
otterremo i risultati:
>
> Typing error(s) found.
> 
> Case duplicate in switch case.
>
> @4
>

#### Test struttura dati Array
Dato il programma di esempio che ben rappresenta l'utilizzo della struttura dati _Array_:
```
int y[] <- {81,82};
int x <- 0;
int z <- 0
```
otterremo i risultati:
>
> { x[int]:0 y_1[int]:82 z[int]:0 y_0[int]:81 }
> 
> Type checking superato con successo!
>

#### Test costrutto utilizzo SwitchCase
Dato il programma di esempio che ben rappresenta l'utilizzo del costrutto _SwitchCase_ per la quale dobbiamo ottenere un errore dato da tipo erroneo per l'espressione di riferimento per la selezione:
```
int y[] <- {81,20};
int x <- 0;
int z <- 0;
if(z == 0) then {
    x <- y[1]*3
}
```
otterremo i risultati:
>
> { x[int]:60 y_1[int]:20 z[int]:0 y_0[int]:81 }
> 
> Type checking superato con successo!
>

## 3. Sintassi
La sintassi dell'interprete _PseudoCodeInterpreter_ è definita attraverso una grammatica di tipo CF, implementata attraverso il file ANTLR4 `PseudoCode.g4`, si fornisce nel seguito una rappresentazione formale della stessa:
$$
\begin{aligned}
\langle com \rangle \rightarrow & \ \langle type \rangle \ \text{ID} \ [ \ \text{'<-'} \ \langle exp \rangle ] \\
& \mid \langle type \rangle \ \text{ID} \ \text{'['} \ \text{']'} \ [ \ \text{'<-'} \ \text{'\{'} \ \langle exp \rangle \ (\text{','} \ \langle exp \rangle)^* \ \text{'\}'} ] \\
& \mid \text{ID} \ ( \ ( \text{'['} \ \text{']'} \ \text{'<-'} \ \text{'\{'} \ \langle exp \rangle \ (\text{','} \ \langle exp \rangle)^* \ \text{'\}'} \ ) \mid ( \text{'['} \ (\text{INT} \mid \text{ID}) \ \text{']'} \ \text{'<-'} \ \langle exp \rangle \ ) \ ) \\
& \mid \text{ID} \ \text{'<-'} \ \langle exp \rangle \\
& \mid \text{ID} \ (\text{'+='} \mid \text{'-='}) \ \langle exp \rangle \\
& \mid \text{ID} \ (\text{'++'} \mid \text{'--'}) \\
& \mid \text{'if'} \ \text{'('} \ \langle exp \rangle \ \text{')'} \ \text{'then'} \ \text{'\{'} \ \langle com \rangle \ \text{'\}'} \ [ \ \text{'else'} \ \text{'\{'} \ \langle com \rangle \ \text{'\}'} ] \\
& \mid \text{'switch'} \ \text{'('} \ \langle exp \rangle \ \text{')'} \ \text{'\{'} \ ( \text{'case'} \ (\text{INT} \mid \text{DEC} \mid \text{BOOL} \mid \text{STRING} \mid \text{CHAR})^+ \ \text{':'} \ \text{'\{'} \ \langle com \rangle \ \text{'\}'} )^* \\
& \quad [ \ \text{'default'} \ \text{':'} \ \text{'\{'} \ \langle com \rangle \ \text{'\}'} ] \ \text{'\}'} \\
& \mid \text{'repeat'} \ \text{'while'} \ \text{'('} \ \langle exp \rangle \ \text{')'} \ \text{'\{'} \ \langle com \rangle \ \text{'\}'} \\
& \mid \langle com \rangle \ ( \text{'@'} \ \langle com \rangle )^+ \\
& \mid \langle com \rangle \ \text{';'} \ \langle com \rangle \\
& \mid \text{'disp'} \ \langle exp \rangle \\[1.5ex]
%
\langle exp \rangle \rightarrow & \ \langle num \rangle \mid \text{BOOL} \mid \text{STRING} \mid \text{CHAR} \mid \text{ID} \\
& \mid \langle exp \rangle \ \text{'\^{}'} \ \langle exp \rangle \\
& \mid \langle exp \rangle \ (\text{'*'} \mid \text{'/'} \mid \text{'mod'}) \ \langle exp \rangle \\
& \mid \langle exp \rangle \ (\text{'+'} \mid \text{'-'}) \ \langle exp \rangle \\
& \mid \langle exp \rangle \ (\text{'<'} \mid \text{'<='} \mid \text{'>='} \mid \text{'>'}) \ \langle exp \rangle \\
& \mid \langle exp \rangle \ (\text{'=='} \mid \text{'!='}) \ \langle exp \rangle \\
& \mid \langle exp \rangle \ (\text{'AND'} \mid \text{'OR'}) \ \langle exp \rangle \\
& \mid \text{'NOT'} \ \langle exp \rangle \\
& \mid \text{'('} \ \langle exp \rangle \ \text{')'} \\
& \mid \langle exp \rangle \ \text{'?'} \ \langle exp \rangle \ \text{':'} \ \langle exp \rangle \\
& \mid \text{'read'} \ \langle type \rangle \\
& \mid \text{ID} \ \text{'['} \ \text{INT} \ \text{']'} \\[1.5ex]
%
\langle num \rangle \rightarrow & \ \text{INT} \mid \text{DEC} \\[1.5ex]
%
\langle type \rangle \rightarrow & \ \text{'int'} \mid \text{'dec'} \mid \text{'bool'} \mid \text{'string'} \mid \text{'char'}
\end{aligned}
$$
L'insieme dei simboli terminali è ben rappresentato da:
$$
\cdots \textit{ terminare }\cdots
$$

La categoria sintattica dei **comandi** è ben rappresentata da _com_ nella grammatica ANTLR4, che rappresenta inoltre il simbolo iniziale della grammatica. La categoria sintattica delle espressioni è ben caratterizzata in termini semantici dai tipi definiti esplicitamente attraverso il concetto di `Type` (definito nell' opportuno package _type_) e implementati attaverso la interfaccia con i semplici _valori_ nella rappresentazione data dalla macchina astratta di livello inferiore nella gerarchia delle macchine astratte (nel nostro caso è la macchina ospitante l'esecuzione ovvero il linguaggio Java).



### 3.1 Struttura di un programma

### 3.2 Tipi di dato

| Tipo     | Descrizione           | Esempio di valore |
|----------|-----------------------|-------------------|
| `int`    | Intero con segno      | `42`, `-3`        |
| `dec`    | Numero in virgola mobile | `3.14`, `-0.5`    |
| `bool`   | Valore booleano       | `true`, `false`   |
| `string` | Stringa di testo      | `"ciao"`          |
| `char`   | carattere ASCII       | `'a'`              |

I tipi `int` e `dec` sono tipi rappresentati nel contesto sintattico del nostro linguaggio attraverso il concetto di `num`, per la quale nella grammatica è possibile rappresentare sequenze di tali caratteri in modo che combinati diano luogo ai tipi rappresentati in tabella. I tipi `int` e `dec` sono resi compatibili tra loro nelle espressioni aritmetiche con `Int` e tra loro (attraverso conversione implicita svolta in `TypeUtils/castValue()`).

Nell'ottica della tipizzazione è utile notare che il linguaggio PseudoCode da noi definito è in grado di gestire particolari strutture di `array`, che però non sono trattate esplicitamente come tipo, seppur la loro semantica statica e dinamica preveda tutti i controlli del caso per l'attuazione di operazioni su tali oggetti. Infatti per un oggetto array nel linguaggio sono possibili solo le possibilità di dichiarazione e assegnamento, predisponendo dunque un particolare tipo di `id`. D'altro canto gli elementi di array di un particolare tipo sono soggetti ai vincoli di integrità di tipo relizzati per i tipi di base.

Nell'ottica delle memorizzazione di variabili in locazioni di memoria si è utilizzato l'oggetto _id_ come rappresentante del concetto di identificatore di locazione di memoria (ben definito in termini di semantica), che a sua volta è rappresentata da una classe **Mem** che rappresenta un insieme di locazioni di memoria fortemente tipate.
### 3.3 Dichiarazione di variabili
Forniamo di seguito la sintassi prevista per la dichiarazione di variabili, suddivise in base al tipo di riferimento:
```
int x;       // inizializzata al valore di default 0.0
int y <- 3;
int z <- x + y * 2;
bool a <- true;
string b <- "prova";
char c <- 'p';
dec w <- z / 2.0
```

#### 3.3 Dichiarazione di array di variabili
Forniamo di seguito la sintassi prevista per la dichiarazione di array di variabili, suddivisi in base al tipo di riferimento:
```
abc
```

### 3.4 Assegnamenti

```
x = 42;       // assegnamento a variabile di tipo int
x += 5;       // equivalente a x = x + 5
x -= 2;       // equivalente a x = x - 2
x *= 3;       // equivalente a x = x * 3
x /= 4;       // equivalente a x = x / 4
x++;          // post-incremento, incremento unitario dopo la valutazione
x--;          // post-decremento, decremento unitario dopo la valutazione
x <- {1,2,3}  // assegnamento ad array di variabili di tipo int già dichiarato
x[0] <- 1     // assegnamento ad elemento di un array di variabili di tipo int già dichiarato
```

Come statement isolati, post incremento/decremento hanno lo stesso effetto di un assegnamento. La distinzione è rilevante solo quando usati come sotto-espressioni.

### 3.5 Espressioni
Le espressioni supportano la **precedenza standard** degli operatori (dal più basso al più alto):

| Livello | Operatori                                                                       |
|---------|---------------------------------------------------------------------------------|
| 1       | `_ ? _ : _` (ternario)                                                          |
| 2       | `OR`, `AND` (or, and logici)                                                    |
| 3       | `==`, `!=`, `<`, `>`, `<=`, `>=` (comparatori)                                  |
| 4       | `+`, `-` (somma, sottrazione)                                                   |
| 5       | `*`, `/`, `mod`, `pow` (moltiplicazione, divisione, modulo, elevamento a potenza) |
| 6       | `!=` (negazione logica dell'uguaglianza)                |
| 7       | Accesso array `arr[i]`                                                          |

**Operatore ternario:**
```
String msg = (porzioni > 1) ? "porzioni" : "porzione";
```
La variabile `msg` viene assegnata con la stringa *"porzioni"* se la variabile `porzioni` è maggiore di 1. VIene assegnato con la stringa *"porzione"* altrimenti.

### 3.6 Costrutti di controllo
**Condizionale o di Selezione:**
```
if (a > 1) {
    disp "a maggiore di 1";
} else {
    disp "a minore o uguale a 1";
}
```
**Condizionale o di selezione a selezione multipla:**
```
switch(y){
    case "primo":{
        disp "Prima opzione"
    }
    case "secondo":{
        disp "Seconda opzione"
    }
    default:{
        disp "Opzione di default"
    }
}
```

**Ciclo while:**
```
Int i = 0;
repeat while (counter < 5) {
    counter++
}
```

Il ciclo `repeat while (counter < 5)` itera `counter` da `0` a `4` inclusi, i valori su cui iterare possono essere solo numeri interi non negativi.

### 3.7 Stampa e interpolazione nelle stringhe
```
string testo <- "ciao";
string s <- "Risultato: ${testo - 5}"
```
Le stringhe interpolate iniziano normalmente come una variabile di tipo stringa con `"` e terminano con `"`. Le espressioni interpolate in stringa sono racchiuse in `${...}`.

### 3.8 Array
```
string settimana[] <- {"lunedi", "martedi", "mercoledi", "giovedi", "venerdi", "sabato", "domenica"};
int lunedi = 1;
disp "il primo giorno della settimana è ${settimana[lunedi]}";

int x[] <- {100, 101, 102}    // dichiarazione di array
x <- {1,2,3}                  // assegnamento ad array di variabili di tipo int già dichiarato
x[0] <- 1                     // assegnamento ad elemento di un array di variabili di tipo int già dichiarato
```

### 3.9 Uscita dal programma
$$
\cdots \text{ completare eventualmente }\cdots
$$

### 3.10 Commenti
```
int a <- 1        // dichiarazione di variabile
if(a == 1) then { // ramo then del costrutto di controllo
    disp "a è uguale a 1"
} else {          // ramo else del costrutto di controllo
    disp "a è diverso da 1"
}
```

### 3.11 Regole lessicali principali
- Gli identificatori iniziano con una lettera o `_`, seguiti da lettere, cifre, o `_`.
- Le keyword sono case-sensitive e riservate (ad esempio, `if`, `while`, `for`, `true`, `false`, i nomi dei tipi).
- I blocchi sono delimitati da `{` e `}`.
- Ogni sequenza di statement composta da almeno due statement termina con `;` (tranne l'ultimo statement, questo espediente viene utilizzato per predisporre un termine di _lookahead_ per la verifica della terminazione del blocco di codice.
- Gli spazi bianchi e le tabulazioni sono ignorati.
- I commenti sono ignorati nell'esecuzione.

## 4. Semantica

### 4.1 Tipizzazione
PseudoCode adotta **tipizzazione statica**: ogni variabile deve essere dichiarata con un tipo esplicito, e il tipo non può cambiare durante l'esecuzione. Le verifiche di tipo avvengono prima di eseguire un programma tramite un **type system** (implementato attraverso la classe _PseudoCodeTS_, il quale riporta errori segnalati con messaggi descrittivi. Gli errori gestiti staticamente includono:

| Errore                        | Messaggio                                                  |
|-------------------------------|------------------------------------------------------------|
| Variabile non dichiarata      | *"Errore di tipo: variabile 'x' usata ma non dichiarata"*  |
| Tipo incompatibile            | *"Errore di tipo: operazione non valida tra Int e Bool"*   |
| Assegnamento di tipo errato   | *"Errore di tipo: atteso Gram, trovato String"*            |

### 4.2 Visibilità e scoping
PseudoCode adotta lo **scoping lessicale** con **shadowing**. Ogni blocco `{ ... }` introduce un nuovo ambiente. Una variabile dichiarata in un blocco interno può avere lo stesso nome di una variabile esterna (la variabile interna la nasconde all'interno del blocco). All'uscita dal blocco, la variabile esterna ritorna visibile.

Gli identificatori (fortemente tipati) a locazione di memoria dichiarati in qualsiasi punto del codice risiedono nella memoria globale (rappresentata dalla struttura dati predisposta nella classe _Mem_) del programma e sono visibili da qualsiasi punto del programma.

### 4.3 Valutazione delle espressioni
Le espressioni sono valutate in modo standard: tutti gli operandi vengono valutati prima dell'applicazione dell'operatore. Gli operatori logici `AND` e `OR` **non** adottano la semantica short-circuit.

### 4.4 Gerarchia dei tipi
$$
\cdots \text{ da terminare }\cdots
$$

### 4.5 Gestione degli errori a runtime
Gli errori a tempo d'esecuzione vengono intercettati dall'`interprete` e dal `type system` e riportati con messaggi esplicativi (implementati a livello di macchina astratta di livello inferiore nella gerarchia delle macchine astratte, attraverso il concetto di **eccezione**), **terminando** il programma (??? OPPURE **senza terminare** bruscamente il programma ???). Gli errori gestiti includono:

| Errore                                                                                                  | Messaggio                                                                                                                                                                                                                                                                                                                                 |
|---------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Divisione per zero                                                                                      | *"Typing error(s) found. Divisione per zero a tempo di esecuzione!"*                                                                                                                                                                                                                                                                      |
| Indice array fuori limite                                                                               | *"Typing error(s) found. Accesso a indice non valido: a_5"*                                                                                                                                                                                                                                                                               |
| Utilizzo di valori case per switch di tipo non conforme all'espressione di riferimento per il costrutto | *"Typing error(s) found. Type mismatch in switch case. @x-y"*, dove $x$ è l'indice di riga del costrutto switch case e $y$ il numero di case in ordine crescente di posizione                                                                                                                                                             |
| Case duplicati nello switch                                                                             | *"Typing error(s) found. Case duplicate in switch case. @x"*, dove $x$ rappresenta l'indice di riga del costrutto switch case di riferimento per l'errore                                                                                                                                                                                 |
| Variabile non dichiarata                                                                                | *"Typing error(s) found. Variable _varName_ not declared. @x"*, dove $\textit{varName}$ rappresenta il nome della variabile a cui ci si riferisce senza precedente dichiarazione, e $x$ rappresenta l'indice di riga dove si trova il riferimento errato                                                                                  |
| Errore di incongruenza di tipo                                                                          | *"Typing error(s) found. Variable _varName_ cannot be assigned with _erroneousTypeError_. @2:12"*, dove $\textit{varName}$ rappresenta il nome della variabile alla quale si sta cercando di riferirsi e $\textit{erroneousTypeError}$ rappresenta il tipo erroneo che si sta utilizzando per riferirsi alla variabile $\textit{varName}$ |

### 4.6 Semantica operazionale

### Introduzione
Date le scelte progettuali effettuate per la implementazione del linguaggio e del relativo interprete: _PseudoCodeInterpreter_ e
data la definizione di linguaggio di programmazione che rappresenta un sistema formale in grado di modellare il concetto
di _Turing-calcolabilità_, per prima cosa sono stati introdotti i costrutti dati da _struttre di assegnamento_, _strutture condizionali_ e _strutture iterative_,
ottenendo dunque un linugaggio che ha caratteristiche di Turing-completezza.

Nella nostra trattazione, l'interprete _PseudoCodeIntp_ descritto nel seguito, è atto ad interpretare un semplice
linguaggio imperativo didattico che mira alla formalizzazione dello _pseudocodice_ opportunamente alle scelte implementative descritte precedentemente (\textbf{TODO : COMPLETARE SUCCESSIVAMENTE},
infatti il nostro obiettivo è modellare programmi definiti come pseudocidce, ovvero sequenze di comandi separati da punti
e virgola: \textit{com1 ; com2 ; $\cdots$} per la quale si impongno le condizioni di Turing-completezza \textbf{TODO : Inserire punti da libro di Fondamenti di turing-completezza}.

Dato un codice di esempio:
$$
\texttt{$i = 0$; while($i < 9$) $\{ i = i + 1 \}$}
$$
i programmi trasformano dunque la memoria in maniera intuitivamente legata a quanto descritto:
$$
[i \mapsto 3] \overset{[\![ \textit{PseudoCodeIntp} ]\!] (i = 0;\texttt{while}(i < 9)\{i = i+1 \})}{\xrightarrow{\hspace{5cm}}} [i \mapsto 9]
$$
Analizziamo formalmente come è descritto la trasformazione della memoria attraverso i sistemi di transizione associati
al sistema formale predisposto dal linguaggio da noi progettato.

###  Definizione di semantica attraverso _sistemi di transizione_:
Il nostro linguaggio è classificabile come **_staticamente tipizzato_**, ciò è stato attuato attraverso un sistema di
tipi per veriﬁcare  la conformità dei tipi in fase di  compilazione:
Per attuare ciò -ovvero un linguaggio imperativo staticamente tipizzato-, si introducono le dichiarazioni dei tipi
delle variabili: _TypedImp_. Deﬁniremo un elenco della forma $\texttt{string } x, \cdots; \texttt{dec } y;$; atto alla
dichiarazione delle variabili tipate. Le variabili hanno  dichiarazioni di tipo singole. Avremo inoltre bisogno di un
sistema di tipi (type system) per il linguaggio, attraverso  il quale attuare inferenza dei tipi per ciascun elemento
dell'albero  di analisi, veriﬁca della conformità dei tipi con  le dichiarazioni delle variabili e la  semantica dei comandi e che funzioni in fase di compilazione (cioè prima di tentare
l'esecuzione di un programma).

Un **type system** è un processo di inferenza che cerca di assegnare tipi alle strutture del programma. Se a un programma
può essere assegnato un tipo (valido), esso è ben tipizzato. Nei casi più semplici, si tratta di una procedura decisionale.
Nella nostra trattazione adotteremo un approccio più pratico, per cui implementiamo il  sistema dei tipi come interprete
del linguaggio, in questo interprete, il calcolo viene effettuato sui tipi anziché sui valori.

Il tipo di espressione viene utilizzato per veriﬁcare la conformità dei comandi al tipo: non viene eseguita alcuna
conversione di tipo. Analizziamo ora la semantica della componente di interprete di tipi appena introdotta: Introucendola opportunamente come
$$
\Gamma \in \mathcal{X}\to \mathcal{T}
$$
si associano le variabili ai tipi. I tipi sono quelli  deﬁniti nel linguaggio: $\texttt{int, dec,}$ $\texttt{bool, string}$:
$$
\textbf{Decl}\ \frac{\text{--}}{\langle \Gamma, \tau x \rangle \to \langle \Gamma[{}^{\tau}/x],\epsilon \rangle}\ \Gamma(x)\text{ undefined}
$$
$$
\textbf{Assign}\ \frac{\langle \Gamma,\texttt{exp}\to \texttt{exp'} \rangle}{\langle \Gamma,\texttt{x = exp} \rangle \to \langle \Gamma, \texttt{x = exp'} \rangle} \qquad \textbf{AssignOp}\ \frac{\text{--}}{\langle \Gamma, x = \tau \rangle \to \langle \Gamma, \epsilon \rangle}\ \tau = \Gamma(x)
$$
in ottica di **_costrutti condizionali_**:
$$
\textbf{If}\ \frac{\langle \Gamma ,\texttt{exp} \rangle \to \texttt{exp'}}{\langle \Gamma, \texttt{if (exp) { com }} \rangle \to \langle \Gamma, \texttt{if (exp') { com }} \rangle}
$$
$$
\textbf{ifElse}\ \frac{\text{--}}{\langle \Gamma,\texttt{if ($\tau$) { com }} \rangle \to \langle \Gamma, \texttt{com} \rangle} \ \tau = \texttt{bool}
$$
A questo punto, cosa succede se \texttt{exp} non restituisce un valore di tipo booleano? L'interprete genera un errore,
ae indicare che la tipizzazione non è riuscita. Infine, analizziamo l'evoluzione della semantica delle sequenze di comandi:
$$
\textbf{Seq}\ \frac{\langle \Gamma , \texttt{com}_1 \rangle \to \langle \Gamma', \texttt{com}_1' \rangle}{\langle \Gamma , \texttt{com}_1 ; \texttt{com}_2 \rangle \to \langle \Gamma', \texttt{com}_1' ; \texttt{com}_2 \rangle} \qquad \qquad \textbf{SeqE}\ \frac{\text{--}}{\langle \Gamma, \epsilon ; \texttt{com}_2 \rangle \to \langle \Gamma , \texttt{com}_2 \rangle}
$$
La valutazione si interrompe quando \texttt{com} restituisce una configurazione vuota $\langle \Gamma,\epsilon \rangle$, ciò significa che il programma è ben tipizzato.

## 5. Implementazione

### 5.1 Struttura del progetto

```
PseudoCode/
├── src/
│   ├── main/
│   │   ├── antlr4/
│   │   │   └── it.univr.pl/
│   │   │       └── PseudoCode.g4    # Grammatica ANTLR4
│   │   └── java/
│   │       └── it.univr.pl/
│   │           ├── exception/       # Classi di errore custom
│   │           │   ├── SwitchCaseException
│   │           │   ├── TypeMismatchException
│   │           │   └── VarDeclarationException
│   │           ├── type/            # Gerarchia dei tipi e controllo
│   │           │   ├── ComType
│   │           │   ├── ErrType
│   │           │   ├── ExpType
│   │           │   ├── SimpleType
│   │           │   ├── Type
│   │           │   └── TypeUtils
│   │           ├── value/           # Gestione dei valori e dei tipi di dato
│   │           │   ├── BoolValue
│   │           │   ├── CharValue
│   │           │   ├── ComValue
│   │           │   ├── DecValue
│   │           │   ├── ExpValue
│   │           │   ├── IntValue
│   │           │   ├── NumValue
│   │           │   ├── StringValue
│   │           │   └── Value
│   │           ├── Main             # Entry point dell'applicazione
│   │           ├── Mem              # Gestione della memoria / Scope
│   │           ├── PseudoCodeIntp   # Interprete (Visitor/Listener)
│   │           └── PseudoCodeTS     # Type System / Type Checker
│   └── test/
│       └── java/
│           └── PseudoCodeUTests     # Unit Test del progetto
```

### 5.2 L'interprete

### 5.3 Gestione dello scope

### 5.4 Zucchero sintattico

### 5.5 Difficoltà tecniche

## 6. EVENTUALI Programmi di test