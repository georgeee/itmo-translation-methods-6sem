package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.ArithExpr;

/**
 * Created by georgeee on 15.05.15.
 */
public class UnaryArithOp extends AbstractUnaryOp<ArithExpr> implements ArithExpr {
    @Getter
    private final Type type;

    public UnaryArithOp(ArithExpr operand, Type type) {
        super(operand);
        this.type = type;
    }

    public enum Type {
        NEG
    }

}
