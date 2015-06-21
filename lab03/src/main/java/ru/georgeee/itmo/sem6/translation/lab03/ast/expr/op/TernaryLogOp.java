package ru.georgeee.itmo.sem6.translation.lab03.ast.expr.op;

import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.BoolExpr;

public class TernaryLogOp extends AbstractTernaryOp<BoolExpr> implements BoolExpr {
    public TernaryLogOp(BoolExpr condition, BoolExpr trueOperand, BoolExpr falseOperand) {
        super(condition, trueOperand, falseOperand);
    }
}
