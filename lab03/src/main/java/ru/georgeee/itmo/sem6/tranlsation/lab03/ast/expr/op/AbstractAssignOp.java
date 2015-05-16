package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.Util;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.Expr;

import java.util.Map;

abstract class AbstractAssignOp<E extends Expr, V extends Var> implements Expr {
    @Getter
    private final V var;
    @Getter
    private final E expr;

    protected AbstractAssignOp(V var, E expr) {
        this.var = var;
        this.expr = expr;
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return Util.union(Util.singletonMap(var), expr.getUsedVars());
    }
}
