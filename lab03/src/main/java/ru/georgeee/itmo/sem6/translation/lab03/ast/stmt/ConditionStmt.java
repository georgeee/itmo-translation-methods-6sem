package ru.georgeee.itmo.sem6.translation.lab03.ast.stmt;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab03.Util;
import ru.georgeee.itmo.sem6.translation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.BoolExpr;

import java.util.Map;

public class ConditionStmt implements Stmt {
    @Getter
    private final BoolExpr condition;
    @Getter
    private final Stmt trueStmt;
    @Getter
    private final Stmt falseStmt;

    public ConditionStmt(BoolExpr condition, Stmt trueStmt, Stmt falseStmt) {
        this.condition = condition;
        this.trueStmt = trueStmt;
        this.falseStmt = falseStmt;
    }

    @Override
    public Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException {
        Util.validate(ctx, condition);
        ctx = trueStmt.validate(ctx);
        if(falseStmt != null){
            ctx = falseStmt.validate(ctx);
        }
        return ctx;
    }
}
