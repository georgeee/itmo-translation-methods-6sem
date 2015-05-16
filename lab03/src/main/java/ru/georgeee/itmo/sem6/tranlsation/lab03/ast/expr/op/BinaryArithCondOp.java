package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.op;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.ArithExpr;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.expr.BoolExpr;

/**
 * Created by georgeee on 15.05.15.
 */
public class BinaryArithCondOp extends AbstractBinaryOp<ArithExpr> implements BoolExpr {
    @Getter
    private final Type type;

    public BinaryArithCondOp(ArithExpr leftOperand, ArithExpr rightOperand, Type type) {
        super(leftOperand, rightOperand);
        this.type = type;
    }

    public enum Type {
        GT, LT, GTE, LTE
    }
}
