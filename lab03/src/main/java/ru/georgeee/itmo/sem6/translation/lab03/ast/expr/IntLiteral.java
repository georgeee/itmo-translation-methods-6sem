package ru.georgeee.itmo.sem6.translation.lab03.ast.expr;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;

import java.util.Collections;
import java.util.Map;

public class IntLiteral implements ArithExpr {
    @Getter
    private final int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Map<String, Var> getUsedVars() {
        return Collections.emptyMap();
    }
}
