package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

public class Attr {
    @Getter
    private final String type;
    @Getter
    private final String name;

    public Attr(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Attr{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
