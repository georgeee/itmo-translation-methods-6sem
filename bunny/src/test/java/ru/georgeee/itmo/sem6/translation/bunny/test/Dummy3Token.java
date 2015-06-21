package ru.georgeee.itmo.sem6.translation.bunny.test;

import ru.georgeee.itmo.sem6.translation.bunny.runtime.Token;

public enum Dummy3Token implements Token<Dummy3Token> {
    NUM, PLUS, MINUS, OBR, CBR, MUL, DIV;

    @Override
    public String getTypeId() {
        return name().toLowerCase();
    }

    @Override
    public Dummy3Token getType() {
        return this;
    }

}
