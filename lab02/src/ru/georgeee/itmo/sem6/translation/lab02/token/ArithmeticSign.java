package ru.georgeee.itmo.sem6.translation.lab02.token;

import lombok.Getter;

public class ArithmeticSign extends Token {
    @Getter
    private final SignType type;

    public ArithmeticSign(SignType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ArithmeticSign{" +
                "type=" + type +
                '}';
    }

}
