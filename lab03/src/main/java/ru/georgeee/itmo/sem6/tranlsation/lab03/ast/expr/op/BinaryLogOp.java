package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.BoolExpr;

public class BinaryLogOp extends AbstractBinaryOp<BoolExpr> implements BoolExpr {
    @Getter
    private final Type type;

    public BinaryLogOp(BoolExpr leftOperand, BoolExpr rightOperand, Type type) {
        super(leftOperand, rightOperand);
        this.type = type;
    }

    public enum Type {
        AND, OR
    }
}
