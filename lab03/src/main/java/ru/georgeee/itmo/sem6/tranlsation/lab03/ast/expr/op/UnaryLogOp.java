package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.BoolExpr;

public class UnaryLogOp extends AbstractUnaryOp<BoolExpr> implements BoolExpr {
    @Getter
    private final Type type;

    public UnaryLogOp(BoolExpr operand, Type type) {
        super(operand);
        this.type = type;
    }

    public enum Type {
        NOT
    }
}
