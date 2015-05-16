package ru.georgeee.itmo.sem6.translation.lab02.reader;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by georgeee on 11.05.15.
 */
public class CharReader extends AbstractSequentialReader<Character, IOException> {
    private final Reader reader;

    public CharReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    protected Character readImpl() throws IOException {
        int c = reader.read();
        if (c == -1) {
            return null;
        }
        return (char) c;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
