package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.Util;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;

import java.util.Map;

public class VarStmt implements Stmt {
    @Getter
    private final Var var;

    public VarStmt(Var var) {
        this.var = var;
    }

    @Override
    public Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException {
        if (ctx.containsKey(var.getName())) {
            Util.validate(var, ctx.get(var.getName()), this);
        }
        ctx.put(var.getName(), var);
        return ctx;
    }
}
