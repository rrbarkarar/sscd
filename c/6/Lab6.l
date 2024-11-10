%{
#include <stdio.h>
#include <string.h>

// Symbol Table Structure
struct SymbolTableEntry {
    int id;
    char name[10];
    char type[10];
} symbolTable[100];

int idCounter = 1;
int symbolCount = 0;

void addToSymbolTable(const char* name, const char* type) {
    for (int i = 0; i < symbolCount; i++) {
        if (strcmp(symbolTable[i].name, name) == 0) {
            return; // Prevent duplicates
        }
    }
    symbolTable[symbolCount].id = idCounter++;
    strncpy(symbolTable[symbolCount].name, name, 10);
    strncpy(symbolTable[symbolCount].type, type, 10);
    symbolCount++;
}

void printSymbolTable() {
    printf("\nSymbol Table:\n");
    printf("ID\tName\tType\n");
    printf("----------------------\n");
    for (int i = 0; i < symbolCount; i++) {
        printf("%d\t%s\t%s\n", symbolTable[i].id, symbolTable[i].name, symbolTable[i].type);
    }
}
%}

%%

"int"|"float"|"char"|"double"    { /* Ignore data types */ }
"+"|"-"|"*"|"/"                  { /* Ignore operators */ }
"="                              { /* Ignore assignment operator */ }
";"                              { /* Ignore end of statement */ }
[a-zA-Z_][a-zA-Z0-9_]*\[         { addToSymbolTable(yytext, "array"); }   /* Array identifier */
[0-9]+\.[0-9]+                   { /* Ignore float literals */ }
"("|")"                          { /* Ignore parentheses */ }
","                              { /* Ignore commas */ }
[a-zA-Z_][a-zA-Z0-9_]*           { addToSymbolTable(yytext, "identifier"); } /* Variable identifier */
[a-zA-Z_][a-zA-Z0-9_]*\(         { addToSymbolTable(yytext, "function"); }   /* Function identifier */

%%

int main() {
    yyin = fopen("sample.txt", "r");
    yylex();
    printSymbolTable();
    return 0;
}

int yywrap() {
    return 1;
}
