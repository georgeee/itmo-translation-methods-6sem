package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.ArithExpr;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.IntVar;

public class AssignArithOp extends AbstractAssignOp<ArithExpr, IntVar> implements ArithExpr {
    public AssignArithOp(IntVar var, ArithExpr expr) {
        super(var, expr);
    }
}
