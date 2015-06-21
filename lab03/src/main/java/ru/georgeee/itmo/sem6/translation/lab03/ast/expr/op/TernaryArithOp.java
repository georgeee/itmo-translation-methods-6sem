package ru.georgeee.itmo.sem6.translation.lab03.ast.expr.op;

import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.ArithExpr;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.BoolExpr;

public class TernaryArithOp extends AbstractTernaryOp<ArithExpr> implements ArithExpr {
    public TernaryArithOp(BoolExpr condition, ArithExpr trueOperand, ArithExpr falseOperand) {
        super(condition, trueOperand, falseOperand);
    }
}
