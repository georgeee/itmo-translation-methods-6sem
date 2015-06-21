package ru.georgeee.itmo.sem6.translation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.ArithExpr;

public class BinaryArithOp extends AbstractBinaryOp<ArithExpr> implements ArithExpr {
    @Getter
    private final Type type;

    public BinaryArithOp(ArithExpr leftOperand, ArithExpr rightOperand, Type type) {
        super(leftOperand, rightOperand);
        this.type = type;
    }

    public enum Type {
        ADD, SUB, MUL, DIV, MOD
    }
}
