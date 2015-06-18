package ru.georgeee.itmo.sem6.translation.bunny.test;

import ru.georgeee.itmo.sem6.translation.bunny.runtime.Token;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.TokenReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public enum DummyToken implements Token<DummyToken> {
    X, ASSIGN, ASTERIX;

    @Override
    public String getTypeId() {
        return name().toLowerCase();
    }

    @Override
    public DummyToken getType() {
        return this;
    }

    public static TokenReader<DummyToken, DummyToken> getReader(DummyToken... tokens) {
        return getReader(Arrays.asList(tokens));
    }

    public static TokenReader<DummyToken, DummyToken> getReader(Iterable<DummyToken> tokens) {
        return getReader(tokens.iterator());
    }

    public static TokenReader<DummyToken, DummyToken> getReader(Iterator<DummyToken> iterator) {
        return new Reader(iterator);
    }

    private static class Reader implements TokenReader<DummyToken, DummyToken> {
        private final Iterator<DummyToken> iterator;

        private Reader(Iterator<DummyToken> iterator) {
            this.iterator = iterator;
        }

        @Override
        public DummyToken nextToken() throws IOException {
            return iterator.hasNext() ? iterator.next() : null;
        }
    }

}
