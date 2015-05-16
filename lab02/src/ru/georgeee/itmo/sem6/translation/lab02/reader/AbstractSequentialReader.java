package ru.georgeee.itmo.sem6.translation.lab02.reader;

/**
 * Created by georgeee on 11.05.15.
 */
public abstract class AbstractSequentialReader<T, E extends Exception> implements SequentialReader<T, E> {
    private T head;

    @Override
    public T lookAhead() throws E {
        if (head == null) {
            head = readImpl();
        }
        return head;
    }

    @Override
    public T read() throws E {
        if (this.head != null) {
            T head = this.head;
            this.head = null;
            return head;
        }
        return readImpl();
    }

    protected abstract T readImpl() throws E;
}
