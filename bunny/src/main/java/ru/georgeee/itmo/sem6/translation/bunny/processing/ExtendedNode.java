package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;

class ExtendedNode implements Node {
    @Getter
    private final int nodeId;
    @Getter
    private final ItemSet from;
    @Getter
    private final ItemSet to;
    private final Node node;

    ExtendedNode(int nodeId, ItemSet from, ItemSet to, Node node) {
        this.nodeId = nodeId;
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

    public String toString() {
        return "<" + from.getId()
                + ", " + node.toString()
                + ", " + (to == null ? null : to.getId())
                + '>';
    }

    @Override
    public String getId() {
        return node.getId();
    }

    @Override
    public boolean isTerminal() {
        return node.isTerminal();
    }

    @Override
    public boolean isNonterminal() {
        return node.isNonterminal();
    }

    @Override
    public Node unwrap() {
        return node.unwrap();
    }
}
