package ru.georgeee.itmo.sem6.translation.lab02.token;

import lombok.Getter;

public class Num extends Token {
    @Getter
    private int value;

    public Num(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Num{" +
                "value=" + value +
                '}';
    }
}
