#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>
 
#define MAXLEN 100

char input[MAXLEN];
int pos = 0;

void advance(){
    pos++;
}

char input_symbol() {
    return input[pos];
}

void error() {
    printf("Error: String rejected.\n");
    exit(1);
}

void match(char expected) {
    if (input_symbol() == expected) {
        advance();
    } 
    else {
        error();
    }
}

void E();
void E_prime();
void T();
void T_prime();
void F();

void E() {
    printf("Rule applied: E -> T E'\n");
    printf("E is calling T\n");
    T();
    printf("E is calling E'\n");
    E_prime();    
}

void E_prime() {
    printf("Rule applied: E' -> + T E' | epsilon\n");
    if (input_symbol() == '+') {
        printf("Matched +\n");
        advance();
        printf("E' is calling T\n");
        T();
        printf("E' is calling itself E'\n");
        E_prime();
    }
    else {
        printf("Rule applied: E' -> epsilon\n");
    }
}

void T() {
    printf("Rule applied: T -> F T'\n");
    printf("T is calling F\n");
    F();
    printf("T is calling T'\n");
    T_prime();
}

void T_prime() {
    printf("Rule applied: T' -> * F T' | epsilon\n");
    if (input_symbol() == '*') {
        printf("Matched *\n");
        advance();
        printf("T' is calling F\n");
        F();
        printf("T' is calling itself T'\n");
        T_prime();
    }
    else {
        printf("Rule applied: T' -> epsilon\n");
    }
}

void F() {
    printf("Rule applied: F -> ( E ) | id\n");
    if (input_symbol() == 'i' && input[pos+1] == 'd') {
        printf("Matched id\n");
        advance();
        advance();
    }
    else if (input_symbol() == '(') {
        printf("Matched (\n");
        advance();
        printf("F is calling E\n");
        E();
        if (input_symbol() == ')') {
            printf("Matched )\n");
            advance();
        }
        else {
            error();
        }
    }
    else {
        error();
    }
}

int main() {
    printf("Enter string: ");
    fgets(input, MAXLEN, stdin);
    input[strcspn(input, "\n")] = '\0';
    
    printf("Start symbol: E\n\n");
    E();

    if (input[pos] == '\0') {
        printf("\nString accepted.\n");
    }
    else {
        error();
    }

    return 0;
}
