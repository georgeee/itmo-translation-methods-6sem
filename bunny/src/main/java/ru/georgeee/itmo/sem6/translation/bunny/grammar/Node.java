package ru.georgeee.itmo.sem6.translation.bunny.grammar;

public interface Node {
    String getId();
    int getNodeId();
    boolean isTerminal();
    boolean isNonterminal();
    Node unwrap();
}

