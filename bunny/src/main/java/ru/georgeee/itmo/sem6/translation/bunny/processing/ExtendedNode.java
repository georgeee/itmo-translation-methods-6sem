package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;

import java.io.IOException;

class ExtendedNode {
    @Getter
    private final int id;
    @Getter
    private final ItemSet from;
    @Getter
    private final ItemSet to;
    @Getter
    private final Node node;

    ExtendedNode(int id, ItemSet from, ItemSet to, Node node) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.node = node;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    public void print(Appendable out) throws IOException {
        out.append('<').append(String.valueOf(from.getId()))
                .append(", ").append(node.getId())
                .append(", ").append(to == null ? null : String.valueOf(to.getId()))
                .append(String.valueOf('>'));
    }
}
