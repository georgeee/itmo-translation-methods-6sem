package ru.georgeee.itmo.sem6.translation.bunny.parser;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

enum PSym {
    PACKAGE, CLASS, HEADER, START, ENUM_TYPE, TOKEN_TYPE,
    TERMINAL_ID, DOT_SEPARATED_ID, ID, COLON, SEMICOLON, O_SQ_BRACKET, C_SQ_BRACKET,
    L_ANGLE, R_ANGLE, COMMA, IMPL, OR, QUEST, CODE_BLOCK;

    @Getter
    private final String id;

    PSym() {
        String[] parts = name().toLowerCase().split("_");
        String id = "";
        for (String part : parts) {
            id += StringUtils.capitalize(part);
        }
        this.id = id;
    }

    PSym(String id) {
        this.id = id;
    }
}
