package ru.georgeee.itmo.sem6.translation.bunny.grammar;

public interface IProduction<N extends Node> extends Iterable<N> {
    public Node getParent();

    public N get(int index);

    public int size();

    public boolean isEmpty();
}
