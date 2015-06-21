package ru.georgeee.itmo.sem6.translation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.BoolExpr;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.Expr;

public class EqualityCondOp<T extends Expr> extends AbstractBinaryOp<T> implements BoolExpr {
    @Getter
    private final Type type;

    public EqualityCondOp(T leftOperand, T rightOperand, Type type) {
        super(leftOperand, rightOperand);
        this.type = type;
    }

    public enum Type {
        EQ, NEQ
    }
}
