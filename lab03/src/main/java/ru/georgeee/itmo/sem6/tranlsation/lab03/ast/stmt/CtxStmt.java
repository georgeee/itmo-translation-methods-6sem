package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;

import java.util.HashMap;
import java.util.Map;

public class CtxStmt implements Stmt {
    @Getter
    private final Stmt stmt;

    public CtxStmt(Stmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException {
        Map<String, Var> mapCopy = new HashMap<>(ctx);
        stmt.validate(ctx);
        return mapCopy;
    }
}
