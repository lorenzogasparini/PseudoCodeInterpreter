# Documentazione di progetto: _PseudoCodeInterpreter_

## Definizione di semantica attraverso _sistemi di transizione_:
### Introduzione
Date le scelte progettuali effettuate per la implementazione del linguaggio e del relativo interprete: _PseudoCodeInterpreter_ e 
data la definizione di linguaggio di programmazione che rappresenta un sistema formale in grado di modellare il concetto
di _Turing-calcolabilità_, per prima cosa sono stati introdotti i costrutti dati da _struttre di assegnamento_, _strutture condizionali_ e _strutture iterative_,
ottenendo dunque un linugaggio che ha caratteristiche di Turing-completezza.

Nella nostra trattazione, l'interprete \textit{PseudoCodeIntp} descritto nel seguito, è atto ad interpretare un semplice
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

### Definizione del sistema di transizione asociato
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
\subsubsection{Funzionalità Avanzate: Interpolazione, Non Determinismo e Input}
Il linguaggio estende le sue funzionalità base con costrutti avanzati la cui semantica statica (Type System) richiede regole di inferenza dedicate.

Per lo \textbf{zucchero sintattico dell'interpolazione delle stringhe}, il Type System deve analizzare l'espressione \texttt{exp} contenuta all'interno del blocco \texttt{\$\{...\}} per garantirne la corretta tipizzazione prima dell'esecuzione:
$$
\textbf{StringInterp}\ \frac{\langle \Gamma, \texttt{exp} \rangle \to \tau}{\langle \Gamma, \texttt{"... \$\{exp\} ..."} \rangle \to \texttt{string}}
$$

Il costrutto di \textbf{scelta non deterministica} (operatore \texttt{@}) permette di eseguire un comando estratto da una lista con probabilità uniforme. Staticamente, ciò impone che ogni ramo della scelta sia un comando ben tipizzato:
$$
\textbf{NonDet}\ \frac{\langle \Gamma, \texttt{com}_1 \rangle \to \langle \Gamma, \epsilon \rangle \quad \dots \quad \langle \Gamma, \texttt{com}_n \rangle \to \langle \Gamma, \epsilon \rangle}{\langle \Gamma, \texttt{com}_1 @ \dots @ \texttt{com}_n \rangle \to \langle \Gamma, \epsilon \rangle}
$$
A livello di semantica operativa (valutazione a runtime sulla memoria $M$), il sistema di transizione sceglie un indice $i \in \{1, \dots, n\}$ in modo pseudo-casuale, proseguendo l'esecuzione solo con il ramo $\texttt{com}_i$ con probabilità $p = \frac{1}{n}$.

Infine, per il costrutto di \textbf{input da tastiera}, la dichiarazione esplicita del tipo desiderato garantisce che l'espressione assuma staticamente il tipo indicato, delegando l'acquisizione del valore a tempo di esecuzione:
$$
\textbf{Read}\ \frac{\text{--}}{\langle \Gamma, \texttt{read } \tau \rangle \to \tau}
$$