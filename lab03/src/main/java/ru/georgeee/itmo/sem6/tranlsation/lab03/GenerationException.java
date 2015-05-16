package ru.georgeee.itmo.sem6.tranlsation.lab03;

public class GenerationException extends Exception{
    public GenerationException() {
    }

    public GenerationException(String message) {
        super(message);
    }

    public GenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerationException(Throwable cause) {
        super(cause);
    }
}
