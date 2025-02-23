#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

#define N 5000
char _buffer[N] = "";
char _tmp[10][N];

int x(int i);
char* test1(char* s);
char* test2(char* s);
char* test3(char* s);
char* test4(char* s);
char* test5(char* s);
char* test6(int n);
char* test7(bool b);
char* test8(char* s1, char* s2);
char* test9(char* s);
char* check(char* s);
char* logFunc(char* s);
char* complexFunc();
char* test10(char* s);
char* test11(char* s);
char _x[N];
char y[N];
char a[N];
char b[N];
char z[N];
char result[N];
char flag[N];
char output[N];
char status[N];
char log[N];
char res[N];
int counter = 0;

int main(void) {
snprintf(_buffer, N, "%s%s", "ciao", "luigi"), strncpy(_tmp[0], _buffer, N-1), _tmp[0][N-1] = '\0';
snprintf(_buffer, N, "%s%s", test2(test2(_tmp[0])), "rosa"), strncpy(_tmp[1], _buffer, N-1), _tmp[1][N-1] = '\0';
strncpy(_x, _tmp[1], N-1);
snprintf(_buffer, N, "%s%s", "mango", "loco"), strncpy(_tmp[2], _buffer, N-1), _tmp[2][N-1] = '\0';
snprintf(_buffer, N, "%s%s", test1(_tmp[2]), "borlotto"), strncpy(_tmp[3], _buffer, N-1), _tmp[3][N-1] = '\0';
snprintf(_buffer, N, "%s%s", test3(test2(_tmp[3])), "casotto"), strncpy(_tmp[4], _buffer, N-1), _tmp[4][N-1] = '\0';
strncpy(y, _tmp[4], N-1);
strncpy(a, "hello", N-1);
strncpy(b, "world", N-1);
snprintf(_buffer, N, "%s%s", a, b), strncpy(_tmp[5], _buffer, N-1), _tmp[5][N-1] = '\0';
snprintf(_buffer, N, "%s%s", "CAS8", "T3"), strncpy(_tmp[6], _buffer, N-1), _tmp[6][N-1] = '\0';
snprintf(_buffer, N, "%s%s", test4(_tmp[5]), test5(_tmp[6])), strncpy(_tmp[7], _buffer, N-1), _tmp[7][N-1] = '\0';
strncpy(z, _tmp[7], N-1);
snprintf(_buffer, N, "%s%s", test6(((3) + (5)) * ((2) - (1))), "result"), strncpy(_tmp[8], _buffer, N-1), _tmp[8][N-1] = '\0';
strncpy(result, _tmp[8], N-1);
snprintf(_buffer, N, "%s%s", test7((true) && (false)), "boolean"), strncpy(_tmp[9], _buffer, N-1), _tmp[9][N-1] = '\0';
strncpy(flag, _tmp[9], N-1);
snprintf(_buffer, N, "%s%s", "raffaele", "tony"), strncpy(_tmp[0], _buffer, N-1), _tmp[0][N-1] = '\0';
snprintf(_buffer, N, "%s%s", "antonio", test9(_tmp[0])), strncpy(_tmp[1], _buffer, N-1), _tmp[1][N-1] = '\0';
snprintf(_buffer, N, "%s%s", "daniele", "tentacolo"), strncpy(_tmp[2], _buffer, N-1), _tmp[2][N-1] = '\0';
strncpy(output, test8(_tmp[1], _tmp[2]), N-1);
snprintf(_buffer, N, "%s%s", "casotto", "borlotto"), strncpy(_tmp[3], _buffer, N-1), _tmp[3][N-1] = '\0';
if (strcmp((check(_tmp[3])), ("success")) == 0) {
strncpy(status, "approved", N-1);
} else {
strncpy(status, "rejected", N-1);
}
while ((counter) < (3)) {
snprintf(_buffer, N, "%s%d", "iteration", counter), strncpy(_tmp[4], _buffer, N-1), _tmp[4][N-1] = '\0';
strncpy(log, logFunc(_tmp[4]), N-1);
counter = (counter) + (1);
}
printf("%d\n", x(x(1)));
strncpy(res, complexFunc(), N-1);
return 0;
}

int x(int i) {
return (i) + (1);
}

char* test1(char* s) {
snprintf(_buffer, N, "%s%s", s, "_T1"), strncpy(_tmp[5], _buffer, N-1), _tmp[5][N-1] = '\0';
return _tmp[5];
}

char* test2(char* s) {
snprintf(_buffer, N, "%s%s%s", "[", s, "]"), strncpy(_tmp[6], _buffer, N-1), _tmp[6][N-1] = '\0';
return _tmp[6];
}

char* test3(char* s) {
snprintf(_buffer, N, "%s%s", s, "_T3"), strncpy(_tmp[7], _buffer, N-1), _tmp[7][N-1] = '\0';
return _tmp[7];
}

char* test4(char* s) {
snprintf(_buffer, N, "%s%s", "Test4-", s), strncpy(_tmp[8], _buffer, N-1), _tmp[8][N-1] = '\0';
return _tmp[8];
}

char* test5(char* s) {
snprintf(_buffer, N, "%s%s", "Result5:", s), strncpy(_tmp[9], _buffer, N-1), _tmp[9][N-1] = '\0';
return _tmp[9];
}

char* test6(int n) {
snprintf(_buffer, N, "%s%d", "Num:", n), strncpy(_tmp[0], _buffer, N-1), _tmp[0][N-1] = '\0';
return _tmp[0];
}

char* test7(bool b) {
if (b) {
return "TRUE";
} else {
return "FALSE";
}
}

char* test8(char* s1, char* s2) {
snprintf(_buffer, N, "%s%s%s", s1, "-", s2), strncpy(_tmp[1], _buffer, N-1), _tmp[1][N-1] = '\0';
return _tmp[1];
}

char* test9(char* s) {
snprintf(_buffer, N, "%s%s", "Nested-", s), strncpy(_tmp[2], _buffer, N-1), _tmp[2][N-1] = '\0';
return _tmp[2];
}

char* check(char* s) {
if (strcmp((s), ("casottoborlotto")) == 0) {
return "success";
} else {
return "fail";
}
}

char* logFunc(char* s) {
snprintf(_buffer, N, "%s%s", "Log: ", s), strncpy(_tmp[3], _buffer, N-1), _tmp[3][N-1] = '\0';
return _tmp[3];
}

char* complexFunc() {
snprintf(_buffer, N, "%s%s", "deep", "level"), strncpy(_tmp[4], _buffer, N-1), _tmp[4][N-1] = '\0';
snprintf(_buffer, N, "%s%s", "nested", test11(_tmp[4])), strncpy(_tmp[5], _buffer, N-1), _tmp[5][N-1] = '\0';
snprintf(_buffer, N, "%s%s", "start", test10(_tmp[5])), strncpy(_tmp[6], _buffer, N-1), _tmp[6][N-1] = '\0';
return _tmp[6];
}

char* test10(char* s) {
snprintf(_buffer, N, "%s%s", "T10:", s), strncpy(_tmp[7], _buffer, N-1), _tmp[7][N-1] = '\0';
return _tmp[7];
}

char* test11(char* s) {
snprintf(_buffer, N, "%s%s", "T11:", s), strncpy(_tmp[8], _buffer, N-1), _tmp[8][N-1] = '\0';
return _tmp[8];
}

