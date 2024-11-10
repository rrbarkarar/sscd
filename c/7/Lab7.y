%{
#include <stdio.h>
extern int yylex();
extern int yywrap();
%}

%token IF OP CP CMP SC ASG ID NUM OPR CCP OCP WH
 
%% 
start: sif | sw;
sif: IF OP cmpn CP stmt {printf("VALID STATEMENT IF\n") ;}; 
sw: WH OP cmpn CP OCP stmt CCP SC {printf("VALID STATEMENT WHILE\n") ;}; 
cmpn: ID CMP ID {printf("Rule 1\n");}| ID CMP NUM {printf("Rule 2\n");};
stmt: ID ASG ID SC {printf("Rule 3\n");}| ID ASG ID OPR NUM SC {printf("Rule 4\n");}| ID ASG NUM OPR ID SC {printf("Rule 5\n");}| ID ASG NUM OPR NUM SC {printf("Rule 6\n");}| ID ASG NUM SC{printf("Rule 7\n");};

%%
int yyerror (char *str)
{ 
	printf("%s", str);
}
int main()
{
	yyparse();
	return 1;
}
