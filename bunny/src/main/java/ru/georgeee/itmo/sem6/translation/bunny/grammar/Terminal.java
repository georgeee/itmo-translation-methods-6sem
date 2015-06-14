package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

public class Terminal implements Node{
    private static final int CLASS_HASH_CODE_SALT = Terminal.class.hashCode();
    @Getter
    private final String id;

    public Terminal(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Terminal terminal = (Terminal) o;

        if (!id.equals(terminal.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + CLASS_HASH_CODE_SALT;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "id='" + id + '\'' +
                '}';
    }
}
