grammar Grammar;

@header{
package ru.georgeee.itmo.sem6.translation.bunny.grammar;
import static ru.georgeee.itmo.sem6.translation.bunny.parser.ParserHelper.*;
import ru.georgeee.itmo.sem6.translation.bunny.parser.*;
}

grammarDef returns [Grammar v] : g=grammarDef p=package { $v = $g.v; $g.v.setPackage($p.v); }
                               | g=grammarDef c=class { $v = $g.v; $g.v.setClassName($c.v); }
                               | g=grammarDef h=header { $v = $g.v; addHeader($g.v, $h.v); }
                               | g=grammarDef r=rule { $v = $g.v; addRule($g.v, $r.v); };

package returns [String v] : Package id=Id { $v = $id.text; } | Package id=PackageId { $v = $id.text; };
class returns [String v]: Class id=Id { $v = $id.text; };
header returns [String v]: Header c=CodeBlock { $v = $c.text; };

//Lexer



CodeBlock : '%{' ~('}%')* '}%' {setText($text.substring(2, $text.length() - 2));};
Comment : ( '--' | '//' ) ~( '\r' | '\n')* -> skip;
WS  : [ \t\r\n]+ -> skip ;

Package: '%package';
Class: '%class';
Header: '%header';

fragment _Id : [_A-Za-z][_A-Za-z0-9]*;
PackageId: (Id ('.' Id)+ );
Id : Id;
Colon: ':';
At: '@';
Semicolon: ';';
OSqBracket: '[';
CSqBracket : ']';
Comma: ',';
Impl: '->';
Or: '|';
