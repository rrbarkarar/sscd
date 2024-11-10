%{
#include "y.tab.h"
extern int yyerror(char *str);
extern int yyparse();
%} 
%%
"if" return IF;
"while" return WH;
"(" return OP;
")" return CP;
"{" return OCP;
"}" return CCP;
"<" |
">" |
"<=" |
">=" |
"==" |
"!=" return CMP;
"*" |
"/" |
"-" |
"+" return OPR;
"=" return ASG;
([a-zA-Z])("_"[a-zA-Z0-9])* return ID;
[0-9]+ return NUM;
";" return SC;
" " {}
%%
int yywrap()
{
return 1;
};

