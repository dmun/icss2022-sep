grammar ICSS;

//--- PARSER: ---
stylesheet : variable_assignment* style_rule* EOF;

if_clause   : IF BOX_BRACKET_OPEN variable_name BOX_BRACKET_CLOSE OPEN_BRACE declaration* CLOSE_BRACE else_clause?;
else_clause : ELSE OPEN_BRACE declaration CLOSE_BRACE;

variable_assignment : variable_name ASSIGNMENT_OPERATOR variable_expression SEMICOLON;
variable_name       : CAPITAL_IDENT;
variable_expression : literal | variable_name;

literal : TRUE | FALSE | PIXELSIZE | PERCENTAGE | SCALAR | COLOR | variable_name;

style_rule : selector OPEN_BRACE declaration* CLOSE_BRACE;
selector   : LOWER_IDENT | ID_IDENT | CLASS_IDENT;

declaration   : property_name COLON expression SEMICOLON | if_clause;
property_name : 'color' | 'background-color' | 'width' | 'height';
expression    : literal | expression mul expression | expression add_sub expression ;

operation : operation mul operation | operation add_sub operation | literal | variable_name;
mul       : MUL;
add_sub   : PLUS | MIN;

//--- LEXER: ---

// IF support:
IF                : 'if';
ELSE              : 'else';
BOX_BRACKET_OPEN  : '[';
BOX_BRACKET_CLOSE : ']';


//Literals
TRUE       : 'TRUE';
FALSE      : 'FALSE';
PIXELSIZE  : [0-9]+ 'px';
PERCENTAGE : [0-9]+ '%';
SCALAR     : [0-9]+;


//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT    : '#' [a-z0-9\-]+;
CLASS_IDENT : '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT   : [a-z] [a-z0-9\-]*;
CAPITAL_IDENT : [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS : [ \t\r\n]+ -> skip;

//
OPEN_BRACE  : '{';
CLOSE_BRACE : '}';
SEMICOLON   : ';';
COLON       : ':';
PLUS        : '+';
MIN         : '-';
MUL         : '*';
ASSIGNMENT_OPERATOR: ':=';