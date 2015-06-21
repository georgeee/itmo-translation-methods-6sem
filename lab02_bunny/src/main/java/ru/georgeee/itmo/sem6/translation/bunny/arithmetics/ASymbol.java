package ru.georgeee.itmo.sem6.translation.bunny.arithmetics;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.Token;

public class ASymbol<T> implements Token<ASym> {
    @Getter
    private final ASym type;
    @Getter
    private final int line;
    @Getter
    private final int column;
    @Getter
    private final T value;

    public ASymbol(ASym type, int line, int column) {
        this(type, line, column, null);
    }

    public ASymbol(ASym type, int line, int column, T value) {
        this.type = type;
        this.line = line;
        this.column = column;
        this.value = value;
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
