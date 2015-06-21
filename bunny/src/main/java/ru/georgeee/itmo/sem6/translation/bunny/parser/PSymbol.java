package ru.georgeee.itmo.sem6.translation.bunny.parser;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.Token;

class PSymbol<T> implements Token<PSym> {
    @Getter
    private final PSym type;
    @Getter
    private final int line;
    @Getter
    private final int column;
    @Getter
    private final T value;

    public PSymbol(PSym type, int line, int column) {
        this(type, line, column, null);
    }

    public PSymbol(PSym type, int line, int column, T value) {
        this.type = type;
        this.line = line;
        this.column = column;
        this.value = value;
    }

    public Integer getInt() {
        return (Integer) value;
    }

    public String getText() {
        return (String) value;
    }

    @Override
    public String toString() {
        return "{" + type + " : " + value + '}';
    }

    @Override
    public String getTypeId() {
        return type.getId();
    }
}
