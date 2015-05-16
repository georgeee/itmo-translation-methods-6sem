package ru.georgeee.itmo.sem6.translation.lab02.reader;

/**
 * Reader, which reads objects of type T, can throw E during read()
 * and supports rereading of last read object (i.e. it will be returned once more on next read read()
 * without any other side effects (like touching underlined reader)
 */
public interface SequentialReader<T, E extends Exception> extends AutoCloseable {
    T read() throws E;

    T lookAhead() throws E;

    void close() throws E;
}
