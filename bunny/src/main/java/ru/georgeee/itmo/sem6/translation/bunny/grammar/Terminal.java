package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

public class Terminal implements Node{
    @Getter
    private final String id;

    public Terminal(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "id='" + id + '\'' +
                '}';
    }
}
