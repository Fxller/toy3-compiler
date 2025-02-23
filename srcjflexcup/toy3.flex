package toy3;
import java_cup.runtime.Symbol;

%%
%public
%class Lexer
%cupsym toy3.sym
%cup
%unicode
%line
%column

%{
StringBuffer string = new StringBuffer();
boolean charFlag = false;

private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
}
private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
}

public static char interpretEscape(char escape) {
    switch (escape) {
        case 'b': return '\b';  // Backspace
        case 't': return '\t';  // Tabulazione
        case 'n': return '\n';  // Newline
        case 'f': return '\f';  // Form feed
        case 'r': return '\r';  // Ritorno a capo
        case '\'': return '\''; // Apostrofo (apice singolo)
        case '\"': return '\"'; // Virgolette doppie
        case '\\': return '\\'; // Backslash
        default:
            throw new IllegalArgumentException("Invalid escape sequence: \\" + escape);
    }
}
%}

LineTerminator = \r|\n|\r\n\
WhiteSpace = {LineTerminator} | [ \t\f]
InputCharacter = [^\r\n]
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
Identifier = [:jletter:] [:jletterdigit:]*
INT_CONST = \d+
DOUBLE_CONST = (\d*\.\d+|\d+\.\d*)([eE][+-]?\d+)?

%state STRING
%state COMMENT
%state CHAR

%%
<YYINITIAL> {
"program" { return symbol(sym.PROGRAM); }
"begin" { return symbol(sym.BEGIN); }
"end" { return symbol(sym.END); }
\" {string.setLength(0); yybegin(STRING);}
";" { return symbol(sym.SEMI); }
":" { return symbol(sym.COLON); }
"int" { return symbol(sym.INT); }
"bool" { return symbol(sym.BOOL); }
"double" { return symbol(sym.DOUBLE); }
"string" { return symbol(sym.STRING); }
"char" { return symbol(sym.CHAR); }
"," { return symbol(sym.COMMA); }
"def" { return symbol(sym.DEF); }
"(" { return symbol(sym.LPAR) ; }
")" { return symbol(sym.RPAR); }
"{" { return symbol(sym.LBRAC); }
"}" { return symbol(sym.RBRAC); }
"<<" { return symbol(sym.IN); }
">>" { return symbol(sym.OUT); }
"!>>" { return symbol(sym.OUTNL); }
"/*" { yybegin(COMMENT); }
"+" { return symbol(sym.PLUS); }
"-" { return symbol(sym.MINUS); }
"*" { return symbol(sym.TIMES); }
"/" { return symbol(sym.DIV); }
{INT_CONST} { return symbol(sym.INT_CONST, Integer.parseInt(yytext())); }
{DOUBLE_CONST} { return symbol(sym.DOUBLE_CONST, Double.parseDouble(yytext())); }
\' {string.setLength(0); yybegin(CHAR);}
"true" { return symbol(sym.TRUE, true); }
"false" { return symbol(sym.FALSE, false); }
"return" { return symbol(sym.RETURN); }
":=" { return symbol(sym.ASSIGN); }
"=" { return symbol(sym.ASSIGNDECL); }
"if" { return symbol(sym.IF); }
"then" { return symbol(sym.THEN); }
"while" { return symbol(sym.WHILE); }
"do" { return symbol(sym.DO); }
"else" { return symbol(sym.ELSE); }
">" { return symbol(sym.GT); }
">=" { return symbol(sym.GE); }
"<" { return symbol(sym.LT); }
"<=" { return symbol(sym.LE); }
"==" { return symbol(sym.EQ); }
"<>" { return symbol(sym.NE); }
"not" { return symbol(sym.NOT); }
"and" { return symbol(sym.AND); }
"or" { return symbol(sym.OR); }
"ref" { return symbol(sym.REF); }
"|" { return symbol(sym.PIPE); }
{Identifier} { return symbol(sym.ID, yytext()); }
{WhiteSpace} {/*ignore*/}
{EndOfLineComment} {/*ignore*/}
<<EOF>> { return symbol(sym.EOF); }
}

<STRING> {
    \" {
        yybegin(YYINITIAL);
        return new Symbol(sym.STRING_CONST, string.toString());
    }

    [^\n\r\"\\]+ {
        string.append(yytext());
    }

    \\[btnfr\"\'\\] {
        string.append(interpretEscape(yytext().charAt(1)));
    }

    {WhiteSpace} {/*ignore*/}

    <<EOF>> {
        throw new Error("Stringa costante non completata");
    }
}

<CHAR> {
    \\[btnfr\"\'\\]\' {
        char escapedChar = interpretEscape(yytext().charAt(1));
        yybegin(YYINITIAL);
        return new Symbol(sym.CHAR_CONST, escapedChar);
    }

    [^\'] {
            string.append(yytext());
    }

    \' {
          if (string.length() > 1) {
              throw new Error("Troppi caratteri");
          } else if (string.length() == 0) {
              throw new Error("Carattere vuoto");
          }
          yybegin(YYINITIAL);
          return new Symbol(sym.CHAR_CONST, string.charAt(0));
    }

    <<EOF>> {
        throw new Error("Carattere non completato");
    }
}


<COMMENT> {
    "*/" { yybegin(YYINITIAL); }
    [^*]+ {} // Accumula caratteri del commento
    \*[^/] {} // Gestisce un asterisco che non fa parte di "*/"
    <<EOF>> { throw new Error("Commento non chiuso"); }
}

[^] {throw new Error("Illegal character <" + yytext() + ">");}