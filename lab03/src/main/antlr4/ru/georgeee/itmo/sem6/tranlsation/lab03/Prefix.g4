// define a grammar called Hello
grammar Prefix;

@header{
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.*;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.*;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op.*;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt.*;
}

assignArithExpr returns [AssignArithOp v] : AssignOp arithId arithExpr
         { $v = new AssignArithOp($arithId.v, $arithExpr.v); };
assignBoolExpr returns [AssignLogOp v] : AssignOp boolId boolExpr
         { $v = new AssignLogOp($boolId.v, $boolExpr.v); };

arithId returns [IntVar v] : ArithId { $v = new IntVar($ArithId.text); };
boolId returns [BoolVar v] : BoolId { $v = new BoolVar($BoolId.text); };
intLit returns [IntLiteral v] : Int { $v = new IntLiteral($Int.int); };
boolLit returns [BoolLiteral v] : Bool { $v = new BoolLiteral(Boolean.parseBoolean($Bool.text)); };
uArithOp returns [UnaryArithOp v] : UArithOp arithExpr
         { UnaryArithOp.Type type;
           switch($UArithOp.text){
             case "neg" : type = UnaryArithOp.Type.NEG; break;
             default : throw new AssertionError();
           }
           $v = new UnaryArithOp($arithExpr.v, type);
         };
bArithOp returns [BinaryArithOp v] : BArithOp a = arithExpr b = arithExpr
         { BinaryArithOp.Type type;
           switch($BArithOp.text){
             case "+" : type = BinaryArithOp.Type.ADD; break;
             case "-" : type = BinaryArithOp.Type.SUB; break;
             case "*" : type = BinaryArithOp.Type.MUL; break;
             case "/" : type = BinaryArithOp.Type.DIV; break;
             case "%" : type = BinaryArithOp.Type.MOD; break;
             default : throw new AssertionError();
           }
           $v = new BinaryArithOp($a.v, $b.v, type);
         };

ternaryArithOp returns [TernaryArithOp v] : TernaryOp boolExpr a = arithExpr b = arithExpr
        { $v = new TernaryArithOp($boolExpr.v, $a.v, $b.v); };
bArithCondOp returns [BinaryArithCondOp v] : BArithCondOp a = arithExpr b = arithExpr
         { BinaryArithCondOp.Type type;
           switch($BArithCondOp.text){
             case ">" : type = BinaryArithCondOp.Type.GT; break;
             case "<" : type = BinaryArithCondOp.Type.LT; break;
             case ">=" : type = BinaryArithCondOp.Type.GTE; break;
             case "<=" : type = BinaryArithCondOp.Type.LTE; break;
             default : throw new AssertionError();
           }
           $v = new BinaryArithCondOp($a.v, $b.v, type);
         };
eqArithCondOp returns [EqualityCondOp<ArithExpr> v] : EqCondOp a = arithExpr b = arithExpr
         { EqualityCondOp.Type type;
           switch($EqCondOp.text){
             case "==" : type = EqualityCondOp.Type.EQ; break;
             case "!=" : type = EqualityCondOp.Type.NEQ; break;
             default : throw new AssertionError();
           }
           $v = new EqualityCondOp<ArithExpr>($a.v, $b.v, type);
         };
eqLogCondOp returns [EqualityCondOp<BoolExpr> v] : EqCondOp a = boolExpr b = boolExpr
         { EqualityCondOp.Type type;
           switch($EqCondOp.text){
             case "==" : type = EqualityCondOp.Type.EQ; break;
             case "!=" : type = EqualityCondOp.Type.NEQ; break;
             default : throw new AssertionError();
           }
           $v = new EqualityCondOp<BoolExpr>($a.v, $b.v, type);
         };
ternaryLogOp returns [TernaryLogOp v] : TernaryOp a = boolExpr b = boolExpr c = boolExpr
        { $v = new TernaryLogOp($a.v, $b.v, $c.v); };

uLogOp returns [UnaryLogOp v] : ULogOp boolExpr
         { UnaryLogOp.Type type;
           switch($ULogOp.text){
             case "!" : type = UnaryLogOp.Type.NOT; break;
             default : throw new AssertionError();
           }
           $v = new UnaryLogOp($boolExpr.v, type);
         };
bLogOp returns [BinaryLogOp v] : BLogOp a = boolExpr b = boolExpr
         { BinaryLogOp.Type type;
           switch($BLogOp.text){
             case "&&" : type = BinaryLogOp.Type.AND; break;
             case "||" : type = BinaryLogOp.Type.OR; break;
             default : throw new AssertionError();
           }
           $v = new BinaryLogOp($a.v, $b.v, type);
         };

arithExpr returns [ArithExpr v] : arithId { $v = $arithId.v; }
                    | intLit { $v = $intLit.v; }
                    | bArithOp { $v = $bArithOp.v; }
                    | uArithOp { $v = $uArithOp.v; }
                    | ternaryArithOp { $v = $ternaryArithOp.v; }
                    | assignArithExpr { $v = $assignArithExpr.v; };

boolExpr returns [BoolExpr v] : boolId { $v = $boolId.v; }
                  | boolLit { $v = $boolLit.v; }
                  | bArithCondOp { $v = $bArithCondOp.v; }
                  | eqArithCondOp { $v = $eqArithCondOp.v; }
                  | eqLogCondOp { $v = $eqLogCondOp.v; }
                  | bLogOp { $v = $bLogOp.v; }
                  | uLogOp { $v = $uLogOp.v; }
                  | ternaryLogOp { $v = $ternaryLogOp.v; }
                  | assignBoolExpr { $v = $assignBoolExpr.v; };

expr returns [Expr v] :  arithExpr { $v = $arithExpr.v; }
      | boolExpr { $v = $boolExpr.v; };

read returns [ReadStmt v] : Read boolId { $v = new ReadStmt($boolId.v); } | Read arithId { $v = new ReadStmt($arithId.v); };
print returns [PrintStmt v] : Print (arithExpr {$v = new PrintStmt($arithExpr.v); } | boolExpr {$v = new PrintStmt($boolExpr.v); });
cond returns [ConditionStmt v] : If boolExpr a = stmt b = stmt { $v = new ConditionStmt($boolExpr.v, $a.v, $b.v); };
var returns [VarStmt v] : Var (boolId { $v = new VarStmt($boolId.v); } | arithId { $v = new VarStmt($arithId.v); });
loop returns [LoopStmt v] : Loop boolExpr stmt { $v = new LoopStmt($boolExpr.v, $stmt.v); };
compound returns [CompoundStmt v] : Semicolon a = stmt b = stmt
          {
            Stmt left = $a.v;
            Stmt right = $b.v;
            if(left instanceof CompoundStmt){
              ((CompoundStmt)left).add(right);
              $v = ((CompoundStmt)left);
            }else{
              $v = new CompoundStmt(left, right);
            }
          };
context returns [CtxStmt v] : CtxShifter stmt { $v = new CtxStmt($stmt.v); };

stmt returns [Stmt v] :   loop { $v = $loop.v; }
       | assignArithExpr { $v = new ExprStmt($assignArithExpr.v); }
       | assignBoolExpr { $v = new ExprStmt($assignBoolExpr.v); }
       | read { $v = $read.v; }
       | print { $v = $print.v; }
       | cond { $v = $cond.v; }
       | var { $v = $var.v; }
       | compound { $v = $compound.v; }
       | context { $v = $context.v; };

program returns [CompoundStmt v] : stmt*
    { $v = new CompoundStmt(); for(StmtContext stmtCtx : $ctx.stmt()) $v.add(stmtCtx.v); };



//Lexer

WS  : [ \t\r\n]+ -> skip ;
Comment : ( '--' | '//' ) ~( '\r' | '\n')* -> skip;

Print : 'print';
Read : 'read';
If : 'if';
Loop : 'while';
Var : 'var';
Bool: 'true' | 'false';
UArithOp : 'neg';
BArithOp : [-+*/%];
BArithCondOp : '>'[=]? | '<'[=]?;
BLogOp : '&&' | '||';
ULogOp : '!';
EqCondOp : '==' | '!=';
TernaryOp : '?:';
AssignOp : '=';

Semicolon : ';';
CtxShifter: '@';

ArithId : [_A-Za-z][_A-Za-z0-9]* ;
BoolId : '#'[_A-Za-z][_A-Za-z0-9]*;

Int: [\-]?[0-9]+;
