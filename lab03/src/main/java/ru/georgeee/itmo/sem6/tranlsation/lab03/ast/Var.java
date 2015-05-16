package ru.georgeee.itmo.sem6.tranlsation.lab03.ast;

import lombok.Getter;

public class Var {
    @Getter
    private final String name;

    public Var(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
