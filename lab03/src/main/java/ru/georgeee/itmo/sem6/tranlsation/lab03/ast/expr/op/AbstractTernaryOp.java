package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.Util;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.BoolExpr;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.Expr;

import java.util.Map;

abstract class AbstractTernaryOp<T extends Expr> implements Expr {
    @Getter
    private final BoolExpr condition;
    @Getter
    private final T trueOperand;
    @Getter
    private final T falseOperand;

    protected AbstractTernaryOp(BoolExpr condition, T trueOperand, T falseOperand) {
        this.condition = condition;
        this.trueOperand = trueOperand;
        this.falseOperand = falseOperand;
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return Util.union(condition.getUsedVars(), trueOperand.getUsedVars(), falseOperand.getUsedVars());
    }
}
