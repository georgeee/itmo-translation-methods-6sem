package ru.georgeee.itmo.sem6.translation.lab02.exception;

/**
 * Created by georgeee on 11.05.15.
 */
public class TokenParseException extends ParseException {
    public TokenParseException() {
    }

    public TokenParseException(String message) {
        super(message);
    }

    public TokenParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenParseException(Throwable cause) {
        super(cause);
    }
}
