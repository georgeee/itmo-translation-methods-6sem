package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.Util;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.BoolExpr;

import java.util.Map;

public class LoopStmt implements Stmt {
    @Getter
    private final BoolExpr condition;
    @Getter
    private final Stmt body;

    public LoopStmt(BoolExpr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException {
        Util.validate(ctx, condition);
        return body.validate(ctx);
    }
}
