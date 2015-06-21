package ru.georgeee.itmo.sem6.translation.lab03.ast.expr;

import ru.georgeee.itmo.sem6.translation.lab03.Util;
import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;

import java.util.Map;

public class BoolVar extends Var implements BoolExpr {
    public BoolVar(String name) {
        super(name);
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return Util.singletonMap(this);
    }
}
