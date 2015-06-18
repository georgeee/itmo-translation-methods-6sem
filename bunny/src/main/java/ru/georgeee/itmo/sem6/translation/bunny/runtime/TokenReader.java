package ru.georgeee.itmo.sem6.translation.bunny.runtime;

public interface TokenReader<E extends Enum<E>, T extends Token<E>> {
    T nextToken() throws java.io.IOException;
}
