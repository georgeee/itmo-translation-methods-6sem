package ru.georgeee.itmo.sem6.translation.bunny.test.arithmetics;

import lombok.Getter;

public enum ASym {
    PLUS, MINUS, DIV, MUL,
    NUM, OBRACKET, CBRACKET,
    EOF;

    @Getter
    private final String id;

    ASym() {
        id = name().toLowerCase();
    }

    ASym(String id) {
        this.id = id;
    }
}
