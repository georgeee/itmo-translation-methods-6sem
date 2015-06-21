package ru.georgeee.itmo.sem6.translation.bunny.arithmetics.expr;

import lombok.Getter;

/**
 * Created by georgeee on 11.05.15.
 */
public class UnaryOp implements Expr {
    private final Expr a;
    private final Type type;

    public UnaryOp(Expr a, Type type) {
        this.a = a;
        this.type = type;
    }

    @Override
    public int evaluate() {
        switch (type) {
            case NEG:
                return -a.evaluate();
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return type.getRepresentation() + "(" + a.toString() + ")";
    }

    public enum Type {
        NEG("-");

        @Getter
        private final String representation;

        Type(String representation) {
            this.representation = representation;
        }

    }
}
