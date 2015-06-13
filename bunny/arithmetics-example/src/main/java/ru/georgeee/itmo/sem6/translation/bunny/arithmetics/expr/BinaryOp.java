package ru.georgeee.itmo.sem6.translation.bunny.arithmetics.expr;

import lombok.Getter;

/**
 * Created by georgeee on 11.05.15.
 */
public class BinaryOp implements Expression {
    private final Expression a;
    private final Expression b;
    private final Type type;

    public BinaryOp(Expression a, Expression b, Type type) {
        this.a = a;
        this.b = b;
        this.type = type;
    }

    @Override
    public int evaluate() {
        int aVal = a.evaluate();
        int bVal = b.evaluate();
        switch (type) {
            case ADD:
                return aVal + bVal;
            case SUB:
                return aVal - bVal;
            case DIV:
                return aVal / bVal;
            case MUL:
                return aVal * bVal;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " " + type.getRepresentation() + " " + b.toString() + ")";
    }

    public enum Type {
        ADD("+"), SUB("-"), MUL("*"), DIV("/");

        @Getter
        private final String representation;

        Type(String representation) {
            this.representation = representation;
        }
    }
}
