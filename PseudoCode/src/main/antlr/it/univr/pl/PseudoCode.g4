grammar PseudoCode;

main : com EOF ;

com : TYPE ID (ASSIGN exp)?                                          # decl
    | ID ASSIGN exp                                                  # assign
    | ID op=(PLUS_ASSIGN | MINUS_ASSIGN) exp                         # assignOp  // Avanzato 1: zucchero sintattico
    | ID op=(INC | DEC_OP)                                           # incDec    // Avanzato 1
    | IF LPAR exp RPAR THEN LBRACE com RBRACE (ELSE LBRACE com RBRACE)? # ifElse
    | REPEAT WHILE LPAR exp RPAR LBRACE com RBRACE                   # while
    | com SEMICOLON com                                              # seq
    | OUT exp                                                        # out
    ;

exp : num                                 # numeric
    | BOOL                                # boolean
    | STRING                              # string
    | CHAR                                # char
    | ID                                  # id
    | <assoc=right> exp POW exp           # pow
    | exp op=(MUL | DIV | MOD) exp        # mulDivMod
    | exp op=(ADD | SUB) exp              # addSub
    | exp op=(LT | LEQ | GEQ | GT) exp    # cmpExp
    | exp op=(EQQ | NEQ) exp              # eqExp
    | exp op=(AND | OR) exp               # andOr
    | NOT exp                             # not
    | LPAR exp RPAR                       # parExp
    | exp QMARK exp COLON exp             # ternary     // Avanzato 2: operatore ternario (condizione ? expvero : expfalso)
    ;

num : INT | DEC ;

// Operatori base
ADD : '+'; SUB : '-'; MUL : '*'; DIV : '/'; MOD : 'mod'; POW : '^';
EQQ : '=='; NEQ : '!='; LEQ : '<='; GEQ : '>='; LT : '<'; GT : '>';
NOT : 'NOT'; AND : 'AND'; OR : 'OR';

// Zucchero Sintattico & Ternario (Requisiti Avanzati)
PLUS_ASSIGN : '+=' ;
MINUS_ASSIGN: '-=' ;
INC         : '++' ;
DEC_OP      : '--' ;
QMARK       : '?'  ;
COLON       : ':'  ;

// Parole chiave del Dominio
IF     : 'if' ;
ELSE   : 'else' ;
THEN   : 'then' ;
REPEAT : 'repeat' ;
WHILE  : 'while' ;
ASSIGN : '<-' ;
OUT    : 'disp' ;

// Struttura
LPAR : '(' ; RPAR : ')' ; LBRACE : '{' ; RBRACE : '}' ; SEMICOLON : ';' ;

// Tipi (5 richiesti) e Valori
TYPE   : 'int' | 'dec' | 'bool' | 'string' | 'char' ;
BOOL   : 'T' | 'F' ;
INT    : NAT | '-' POS ;
DEC    : (INT | '-' '0') '.' DIGIT+ ;
STRING : '"' STRCHR* '"' ;
CHAR   : '\'' STRCHR '\'' ;
ID     : [a-z]+ ;

// Frammenti per le espressioni regolari (nascosti al parser)
fragment NAT      : '0' | POS ;
fragment POS      : POSDIGIT DIGIT* ;
fragment DIGIT    : '0' | POSDIGIT ;
fragment POSDIGIT : [1-9] ;
fragment STRCHR   : ~["\\\r\n] | ESC ;
fragment ESC      : '\\' [btnfr"'\\] ;

COMMENT : '//' ~[\r\n]* -> skip ;
WS      : [ \t\r\n]+ -> skip ;