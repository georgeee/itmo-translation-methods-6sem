grammar Grammar;

@header{
import static ru.georgeee.itmo.sem6.translation.bunny.parser.ParserHelper.*;
import org.apache.commons.lang3.tuple.*;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.*;
import java.util.Collections;
}

grammarDef returns [Grammar v] : g=grammarDef p=packageDef { $v = $g.v; $g.v.setPackageName($p.v); }
                               | g=grammarDef c=classDef { $v = $g.v; $g.v.setClassName($c.v); }
                               | g=grammarDef s=startDef { $v = $g.v; $g.v.setStart($s.v); }
                               | g=grammarDef h=headerDef { $v = $g.v; $g.v.addHeaderCodeBlock($h.v); }
                               | g=grammarDef r=ruleDef { $v = $g.v; addRule($g.v, $r.v); }
                               | {$v = new Grammar();} ;

dotSeparatedId returns [String v]: id=Id {$v = $id.text;} | id=DotSeparatedId {$v = $id.text;};

packageDef returns [String v] : Package id=dotSeparatedId { $v = $id.v; };
classDef returns [String v]: Class id=Id { $v = $id.text; };
startDef returns [String v]: Start id=Id { $v = $id.text; };
headerDef returns [String v]: Header c=CodeBlock { $v = $c.text; };
ruleDef returns [PreRule v]: id=Id al=attrList Impl ps=productions Semicolon { $v = new PreRule($id.text, $al.v, $ps.v); };
productions returns [List<PreProduction> v]: p=production { $v = new ArrayList<>(); $v.add($p.v); }
                                           | ps=productions Or p=production { $v = $ps.v; $v.add($p.v); };
production returns [PreProduction v]: ms=productionMembers c=optCodeBlock { $v = new PreProduction($ms.v, $c.v); };
productionMembers returns [List<PreProduction.Member> v]: {$v = new ArrayList<>();}
                                                        | pms=productionMembers pm=productionMember {$v = $pms.v; $v.add($pm.v);};
productionMember returns [PreProduction.Member v]: t=productionMemberId { $v = new PreProduction.Member($t.v, null, $t.type); }
                                                 | a=Id Colon t=productionMemberId { $v = new PreProduction.Member($t.v, $a.text, $t.type); };
productionMemberId returns [String v, boolean type]: id=TerminalId {$v=$id.text; $type=true;}
                                                   | id=Id {$v=$id.text; $type = false;};
optCodeBlock returns [String v]: cb=CodeBlock {$v = $cb.text; } | {$v = null;};

attrList returns [List<Attr> v]: OSqBracket al=attrList_ CSqBracket {$v = $al.v;}
                                               | OSqBracket CSqBracket {$v = Collections.emptyList();}
                                               | {$v = Collections.emptyList();};
attrList_ returns [List<Attr> v]: attr {$v = new ArrayList<>(); $v.add($attr.v);}
                                               | as=attrList_ Comma attr {$v = $as.v; $v.add($attr.v);};

attr returns [Attr v]: t=javaType id=Id {$v = new Attr($t.v, $id.text);};
javaType returns [String v]: id=dotSeparatedId { $v = $id.v; }| id=dotSeparatedId LAngle args=javaTypeArgs RAngle { $v = $id.v + "<" + $args.v + ">"; };
javaTypeArgs returns [String v]: t=javaType {$v = $t.v;} | ts=javaTypeArgs Comma t=javaType {$v = $ts.v + ", " + $t.v;};


//Lexer
CodeBlock : '%{' .*? '}%' {setText(getText().substring(2, getText().length() - 2));};
Comment : ( '--' | '//' ) ~( '\r' | '\n')* -> skip;
MLComment : '/*' .*? '*/' -> skip;
WS  : [ \t\r\n]+ -> skip ;

Package: '%package';
Class: '%class';
Header: '%header';
Start: '%start';

fragment IdF : [_A-Za-z][_A-Za-z0-9]*;
TerminalId: '@' [_A-Za-z0-9]* {setText(getText().substring(1));};
DotSeparatedId: (Id ('.' IdF)+ );
Id : IdF;
Colon: ':';
Semicolon: ';';
OSqBracket: '[';
CSqBracket : ']';
LAngle: '<';
RAngle: '>';
Comma: ',';
Impl: '->' | '→';
Or: '|';
