package ru.georgeee.itmo.sem6.translation.bunny;

public interface TokenReader<T extends Enum<T>> {
    Token<T> nextToken() throws java.io.IOException;
}
