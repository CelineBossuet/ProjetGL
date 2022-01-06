lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.
EOL : '\n' {skip();};

//mots Réservés
PRINT : 'print';
PRINTLN : 'println';
PRINTX : 'printx';
PRINTLNX : 'printlnx';
WHILE : 'while';
RETURN : 'return';
IF : 'if';
ELSE : 'else';
READINT : 'readint';
READFLOAT : 'readfloat';
NEW : 'new';
TRUE : 'true';
FALSE : 'false';
THIS : 'this';
NULL : 'null';
CLASS : 'class';
EXTENDS : 'extends';
PROTECTED : 'protected';
INSTANCEOF : 'instanceof';
ASM : 'asm';

//Symboles Spéciaux
PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
OBRACE : '{';
CBRACE : '}';
SEMI : ';';
COMMA : ',';
EQUALS : '=';
OPARENT : '(';
CPARENT : ')';
OR : '||';
AND : '&&';
EQEQ :'==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';
GT : '>';
LT : '<';
SLASH : '/';
PERCENT : '%';
EXCLAM : '!';
DOT : '.';

//Identificateurs
fragment LETTER: ('a'..'z'|'A'..'Z');
fragment DIGIT : '0' .. '9';
IDENT : (LETTER |  '$' | '_')( LETTER | DIGIT | '$' | '_')*;

//litéraux entiers
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | ('1' .. '9' DIGIT*);

//Litéraux flottants
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-' | ;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM ;
fragment FLOATDEC : DEC ('F' | 'f')? | DEC EXP ('F' | 'f')?;
fragment NUMHEX : (DIGIT | 'a' .. 'f' | 'A' .. 'F')+;
fragment FLOATHEX : ('Ox' | 'OX') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' )?;
FLOAT : FLOATDEC | FLOATHEX;

//Chaine de Caractères
fragment STRING_CAR : ~('"' | '\n' | '\\' );
STRING : '"' ('\\"' | '\\\\' |STRING_CAR)* '"';
MULTI_LINE_STRING : '"' ( ~('"' | '\\') | EOL | '\\"' | '\\\\')* '"';

//commentaires
COMMENT : '//' ( ~('\n') )*   {skip() ;} | '/*' .*? '*/'  {skip() ;};

//Séparateurs
WS : ( ' ' | '\t' | '\r' | '\n' ) {skip ();};

//Inclusion Fichiers
fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"'{
    doInclude(getText());}
    ;

