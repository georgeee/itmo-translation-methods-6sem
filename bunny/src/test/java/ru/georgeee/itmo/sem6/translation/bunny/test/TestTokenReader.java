package ru.georgeee.itmo.sem6.translation.bunny.test;

import ru.georgeee.itmo.sem6.translation.bunny.runtime.Token;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.TokenReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class TestTokenReader<E extends Enum<E>, T extends Token<E>> implements TokenReader<E, T> {
    private final Iterator<T> iterator;

    private TestTokenReader(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public T nextToken() throws IOException {
        return iterator.hasNext() ? iterator.next() : null;
    }

    @SafeVarargs
    public static <E extends Enum<E>, T extends Token<E>> TokenReader getReader(T... tokens) {
        return getReader(Arrays.asList(tokens));
    }

    public static <E extends Enum<E>, T extends Token<E>> TokenReader<E, T> getReader(Iterable<T> tokens) {
        return getReader(tokens.iterator());
    }

    public static <E extends Enum<E>, T extends Token<E>> TokenReader<E, T> getReader(Iterator<T> iterator) {
        return new TestTokenReader<>(iterator);
    }
}
