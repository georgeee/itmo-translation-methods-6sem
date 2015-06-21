package ru.georgeee.itmo.sem6.translation.lab03;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum PSym {
    PRINT, READ, IF, IF_ELSE("IfElse"), LOOP, VAR, BOOL,
    UArithOp("UArithOp"), BArithOp("BArithOp"), BArithCondOp("BArithCondOp"),
    BLogOp("BLogOp"), ULogOp("ULogOp"), EqCondOp("EqCondOp"),
    TernaryOp("TernaryOp"), AssignOp("AssignOp"),
    SEMICOLON, CTX_SHIFTER("CtxShifter"),
    ARITH_ID("ArithId"), BOOL_ID("BoolId"), INT;

    @Getter
    private final String id;

    PSym() {
        id = StringUtils.capitalize(name().toLowerCase());
    }

    PSym(String id) {
        this.id = id;
    }
}
