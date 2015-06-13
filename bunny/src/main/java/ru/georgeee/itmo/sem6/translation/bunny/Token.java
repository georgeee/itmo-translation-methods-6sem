package ru.georgeee.itmo.sem6.translation.bunny;

public interface Token<T extends Enum<T>> {
    String getTypeId();
    T getType();
}
