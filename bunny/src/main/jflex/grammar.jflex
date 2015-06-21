//Enclosed -> Num | ( Open )
//Level0 -> UOp0 Level1 Lev0R
//Level0R -> BOp0 Level0 Level0R | ε
//Level1 -> UOp1 Enclosed Level1R
//Level1R -> BOp1 Level1 Level1R | ε
//BOp0 -> + | -
//BOp1 -> * | /
//UOp0 -> - | ε
//UOp1 -> ε
/* JFlex partial Java language lexer specification */

package ru.georgeee.itmo.sem6.translation.bunny.parser;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.*;
%%

%class GrammarLexer
%implements TokenReader<PSym, PSymbol<?>>
%unicode
%line
%column

%{
  StringBuffer code = new StringBuffer();

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
IdF = [_A-Za-z][_A-Za-z0-9]*
CodeBlock = "%{" .* "}%"
%state CODE_BLOCK

%%

/* keywords */
<YYINITIAL> {

//Lexer
  "%{"                           { code.setLength(0); yybegin(CODE_BLOCK); }

  "%package"                     { return symbol(PSym.PACKAGE); }
  "%class"                       { return symbol(PSym.CLASS); }
  "%header"                      { return symbol(PSym.HEADER); }
  "%start"                       { return symbol(PSym.START); }
  "%enum"                        { return symbol(PSym.ENUM_TYPE); }
  "%token"                       { return symbol(PSym.TOKEN_TYPE); }

  "@" [_A-Za-z0-9]*              { return symbol(PSym.TERMINAL_ID, yytext().substring(1));}
  ({IdF} ("." {IdF})+ )          { return symbol(PSym.DOT_SEPARATED_ID, yytext()); }
  {IdF}                          { return symbol(PSym.ID, yytext()); }
  ":"                            { return symbol(PSym.COLON); }
  ";"                            { return symbol(PSym.SEMICOLON); }
  "["                            { return symbol(PSym.O_SQ_BRACKET); }
  "]"                            { return symbol(PSym.C_SQ_BRACKET); }
  "<"                            { return symbol(PSym.L_ANGLE); }
  ">"                            { return symbol(PSym.R_ANGLE); }
  ","                            { return symbol(PSym.COMMA); }
  "->" | "→"                     { return symbol(PSym.IMPL); }
  "|"                            { return symbol(PSym.OR); }
  "?"                            { return symbol(PSym.QUEST); }
  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<CODE_BLOCK> {
  "}%"                           { yybegin(YYINITIAL); return symbol(PSym.CODE_BLOCK, code.toString()); }
  [^}]                            { code.append(yytext()); }
  "}"                            { code.append(yytext());   }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }
