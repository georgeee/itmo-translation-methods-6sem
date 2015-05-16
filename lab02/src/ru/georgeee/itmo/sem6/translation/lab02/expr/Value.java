package ru.georgeee.itmo.sem6.translation.lab02.expr;

/**
 * Created by georgeee on 11.05.15.
 */
public class Value implements Expression {
    private final int value;

    public Value(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int evaluate() {
        return value;
    }

}
