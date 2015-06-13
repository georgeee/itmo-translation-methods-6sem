package ru.georgeee.itmo.sem6.translation.bunny.arithmetics;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum ASym {
    PLUS, MINUS, DIV, MUL,
    NUM, OBRACKET("OBracket"), CBRACKET("CBracket");
//    EOF;

    @Getter
    private final String id;

    ASym() {
        id = StringUtils.capitalize(name().toLowerCase());
    }

    ASym(String id) {
        this.id = id;
    }
}
