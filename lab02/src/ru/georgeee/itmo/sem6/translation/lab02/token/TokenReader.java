package ru.georgeee.itmo.sem6.translation.lab02.token;

import ru.georgeee.itmo.sem6.translation.lab02.exception.TokenParseException;
import ru.georgeee.itmo.sem6.translation.lab02.reader.AbstractSequentialReader;
import ru.georgeee.itmo.sem6.translation.lab02.reader.CharReader;
import ru.georgeee.itmo.sem6.translation.lab02.reader.SequentialReader;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class TokenReader extends AbstractSequentialReader<Token, TokenParseException> {
    private static final Map<Character, SignType> signMap = createSignMap();
    private final SequentialReader<Character, IOException> charReader;

    public TokenReader(SequentialReader<Character, IOException> charReader) {
        this.charReader = charReader;
    }

    public TokenReader(Reader reader) {
        this(new CharReader(reader));
    }

    private static Map<Character, SignType> createSignMap() {
        Map<Character, SignType> map = new HashMap<>();
        map.put('(', SignType.OPEN_BRACKET);
        map.put(')', SignType.CLOSE_BRACKET);
        map.put('-', SignType.MINUS);
        map.put('+', SignType.PLUS);
        map.put('*', SignType.MUL);
        map.put('/', SignType.DIV);
        return map;
    }


    @Override
    protected Token readImpl() throws TokenParseException {
        try {
            omitSpaces();
            Character ch = charReader.lookAhead();
            if (ch == null) {
                return null;
            }
            if (signMap.containsKey(ch)) {
                charReader.read();
                return new ArithmeticSign(signMap.get(ch));
            } else if (Character.isDigit(ch)) {
                return new Num(readNum());
            } else {
                throw new TokenParseException("Unknown char : " + ch);
            }
        } catch (IOException e) {
            throw new TokenParseException(e);
        }
    }

    private void omitSpaces() throws IOException {
        Character ch = charReader.lookAhead();
        while (ch != null && Character.isWhitespace(ch)) {
            charReader.read();
            ch = charReader.lookAhead();
        }
    }

    private int readNum() throws IOException {
        if (!Character.isDigit(charReader.lookAhead())) {
            throw new IllegalStateException("Failed to read int");
        }
        int result = 0;
        do {
            result = result * 10 + (charReader.read() - '0');
        } while (Character.isDigit(charReader.lookAhead()));
        return result;
    }

    public void close() throws TokenParseException {
        try {
            charReader.close();
        } catch (IOException e) {
            throw new TokenParseException(e);
        }
    }
}
