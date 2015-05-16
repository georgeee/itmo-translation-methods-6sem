package ru.georgeee.itmo.sem6.translation.lab02.exception;

/**
 * Created by georgeee on 11.05.15.
 */
public class ParseException extends Exception {
    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}
