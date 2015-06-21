package ru.georgeee.itmo.sem6.translation.bunny.test;

import ru.georgeee.itmo.sem6.translation.bunny.runtime.Token;

public enum Dummy2Token implements Token<Dummy2Token> {
    C, D;

    @Override
    public String getTypeId() {
        return name().toLowerCase();
    }

    @Override
    public Dummy2Token getType() {
        return this;
    }

}
