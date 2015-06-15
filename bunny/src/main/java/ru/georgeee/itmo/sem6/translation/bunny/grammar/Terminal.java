package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

public class Terminal implements Node{
    @Getter
    private final String id;
    @Getter
    private final int nodeId;
    @Getter
    private final int termId;

    public Terminal(String id, int nodeId, int termId) {
        this.id = id;
        this.nodeId = nodeId;
        this.termId = termId;
    }

    @Override
    public boolean equals(Object o) {
        //Invariant
        return this == o;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "@" + id;
    }
}
