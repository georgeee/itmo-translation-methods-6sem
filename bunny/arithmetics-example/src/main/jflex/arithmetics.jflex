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

package ru.georgeee.itmo.sem6.translation.bunny.arithmetics;
import ru.georgeee.itmo.sem6.translation.bunny.*;
%%

%class ALexer
%implements TokenReader<ASym>
%unicode
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private ASymbol symbol(ASym type) {return new ASymbol(type, yyline, yycolumn);}
  private ASymbol symbol(ASym type, Object value) {return new ASymbol(type, yyline, yycolumn, value);}
%}

%function nextToken
%type ASymbol
%eofval{
  /*return symbol(ASym.EOF);*/
%eofval}
%eofclose

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

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

  /* literals */
  {DecIntegerLiteral}            { return symbol(ASym.NUM, Integer.parseInt(yytext())); }

  /* operators */
  "-"                            { return symbol(ASym.MINUS); }
  "+"                            { return symbol(ASym.PLUS); }
  "*"                            { return symbol(ASym.MUL); }
  "/"                            { return symbol(ASym.DIV); }

  "("                            { return symbol(ASym.OBRACKET);}
  ")"                            { return symbol(ASym.CBRACKET);}
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }
