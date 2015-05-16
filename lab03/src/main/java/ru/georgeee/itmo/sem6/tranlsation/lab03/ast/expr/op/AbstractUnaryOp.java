package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.Expr;

import java.util.Map;

abstract class AbstractUnaryOp<T extends Expr> implements Expr {
    @Getter
    private final T operand;

    public AbstractUnaryOp(T operand) {
        this.operand = operand;
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return operand.getUsedVars();
    }

}
