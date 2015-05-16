package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.Util;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.Expr;

import java.util.Map;

public class PrintStmt implements Stmt {
    @Getter
    private final Expr expr;

    public PrintStmt(Expr expr) {
        this.expr = expr;
    }

    @Override
    public Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException {
        Util.validate(ctx, expr);
        return ctx;
    }
}
