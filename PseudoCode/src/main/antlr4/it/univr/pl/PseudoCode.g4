grammar PseudoCode;

main : com EOF ;

com : TYPE ID (ASSIGN exp)?                                          # decl
    | TYPE ID '[' ']' (ASSIGN LBRACE (exp (',' exp)*) RBRACE)?       # declArray // Avanzato 3: array
    | ID (('[' ']' ASSIGN LBRACE (exp (',' exp)*) RBRACE) | ('[' (INT | ID) ']' ASSIGN exp))          # assignArray // Avanzato 3: array
    | ID ASSIGN exp                                                  # assign
    | ID op=(PLUS_ASSIGN | MINUS_ASSIGN) exp                         # assignOp  // Avanzato 1: zucchero sintattico
    | ID op=(INC | DEC_OP)                                           # incDec    // Avanzato 1: zucchero sintattico
    | IF LPAR exp RPAR THEN LBRACE com RBRACE (ELSE LBRACE com RBRACE)? # ifElse
    | SWITCH LPAR (exp) RPAR LBRACE (CASE op+=(INT | DEC | BOOL | STRING | CHAR) ':' LBRACE com RBRACE)* (DEFAULT ':' LBRACE com RBRACE)? RBRACE  # switch // Avanzato 4: switch
    | REPEAT WHILE LPAR exp RPAR LBRACE com RBRACE                   # while
    | com (NONDET com)+                                              # nonDet // Avanzato 2: non determinismo
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
    | NOT exp                             # not
    | exp op=(LT | LEQ | GEQ | GT) exp    # cmpExp
    | exp op=(EQQ | NEQ) exp              # eqExp
    | exp op=(AND | OR) exp               # andOr
    | LPAR exp RPAR                       # parExp
    | exp QMARK exp COLON exp             # ternary     // Avanzato 1: operatore ternario (condizione ? expvero : expfalso)
    | READ TYPE                           # readExp     // Avanzato 2: input da tastiera
    | ID '[' (INT) ']'                    # arrayVal    // Avanzato 3: array
    ;

num : INT # intNum
    | DEC # decNum ;

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
NONDET : '@' ;
READ   : 'read';
SWITCH : 'switch';
DEFAULT: 'default';
CASE   : 'case';

// Struttura
LPAR : '(' ; RPAR : ')' ; LBRACE : '{' ; RBRACE : '}' ; SEMICOLON : ';' ;

// Tipi e Valori
TYPE   : 'int' | 'dec' | 'bool' | 'string' | 'char' ;
BOOL   : 'T' | 'F' ;
INT    : NAT | '-' POS ;
DEC    : (INT | '-' '0') '.' DIGIT+ ;
STRING : '"' STRCHR* '"' ;
CHAR   : '\'' STRCHR '\'' ;
ID     : ([a-z]|[A-Z])([a-z]|[A-Z]|[0-9])* ;

// Frammenti per le espressioni regolari (nascosti al parser)
fragment NAT      : '0' | POS ;
fragment POS      : POSDIGIT DIGIT* ;
fragment DIGIT    : '0' | POSDIGIT ;
fragment POSDIGIT : [1-9] ;
fragment STRCHR   : ~["\\\r\n] | ESC ;
fragment ESC      : '\\' [btnfr"'\\] ;

COMMENT : '//' ~[\r\n]* -> skip ;
WS      : [ \t\r\n]+ -> skip ;