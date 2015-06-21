//Enclosed -> Num | ( Open )
//Level0 -> UOp0 Level1 Lev0R
//Level0R -> BOp0 Level0 Level0R | ε
//Level1 -> UOp1 Enclosed Level1R
//Level1R -> BOp1 Level1 Level1R | ε
//BOp0 -> + | -
//BOp1 -> * | /
//UOp0 -> - | ε
//UOp1 -> ε
/* JFlex example: partial Java language lexer specification */

package ru.georgeee.itmo.sem6.translation.lab03;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.*;
%%

%class PrefixLexer
%implements TokenReader<PSym, PSymbol<?>>
%unicode
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private PSymbol symbol(PSym type) {return new PSymbol(type, yyline, yycolumn);}
  private PSymbol symbol(PSym type, Object value) {return new PSymbol(type, yyline, yycolumn, value);}
%}

%function nextToken
%type PSymbol
%eofval{
  return null; /*symbol(PSym.EOF);*/
%eofval}
%eofclose

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = [ \t\f] | {LineTerminator}

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

DecIntegerLiteral = 0 | [1-9][0-9]*

%state STRING

%%

/* keywords */
<YYINITIAL> {

  "print"                        { return symbol(PSym.PRINT); }
  "read"                         { return symbol(PSym.READ); }
  "if"                           { return symbol(PSym.IF); }
  "ifelse"                       { return symbol(PSym.IF_ELSE); }
  "while"                        { return symbol(PSym.LOOP); }
  "var"                          { return symbol(PSym.VAR); }
  "true" | "false"               { return symbol(PSym.BOOL, yytext()); }
  "neg"                          { return symbol(PSym.UArithOp, yytext()); }
  [-+*/%]                        { return symbol(PSym.BArithOp, yytext()); }
  ">"[=]? | "<"[=]?              { return symbol(PSym.BArithCondOp, yytext()); }
  "&&" | "||"                    { return symbol(PSym.BLogOp, yytext()); }
  "!"                            { return symbol(PSym.ULogOp, yytext()); }
  "==" | "!="                    { return symbol(PSym.EqCondOp, yytext()); }
  "?:"                           { return symbol(PSym.TernaryOp, yytext()); }
  "="                            { return symbol(PSym.AssignOp, yytext()); }

  ";"                            { return symbol(PSym.SEMICOLON); }
  "@"                            { return symbol(PSym.CTX_SHIFTER); }

  [_A-Za-z][_A-Za-z0-9]*         { return symbol(PSym.ARITH_ID, yytext()); }
  "#"[_A-Za-z][_A-Za-z0-9]*      { return symbol(PSym.BOOL_ID, yytext()); }

  [\-]?[0-9]+                    { return symbol(PSym.INT, Integer.parseInt(yytext())); }

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }
