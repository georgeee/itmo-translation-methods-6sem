package ru.georgeee.itmo.sem6.translation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab03.Util;
import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.Expr;

import java.util.Map;

abstract class AbstractBinaryOp<T extends Expr> implements Expr {
    @Getter
    private final T leftOperand;
    @Getter
    private final T rightOperand;

    public AbstractBinaryOp(T leftOperand, T rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return Util.union(leftOperand.getUsedVars(), rightOperand.getUsedVars());
    }
}
