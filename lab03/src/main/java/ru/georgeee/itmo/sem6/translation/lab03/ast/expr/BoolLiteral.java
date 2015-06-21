package ru.georgeee.itmo.sem6.translation.lab03.ast.expr;

import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;

import java.util.Collections;
import java.util.Map;

public class BoolLiteral implements BoolExpr {
    private final boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return Collections.emptyMap();
    }

    public boolean getValue() {
        return value;
    }
}
