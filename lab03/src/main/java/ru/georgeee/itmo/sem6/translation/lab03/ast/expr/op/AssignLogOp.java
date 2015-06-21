package ru.georgeee.itmo.sem6.translation.lab03.ast.expr.op;

import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.BoolExpr;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.BoolVar;

public class AssignLogOp extends AbstractAssignOp<BoolExpr, BoolVar> implements BoolExpr {
    public AssignLogOp(BoolVar var, BoolExpr expr) {
        super(var, expr);
    }
}
