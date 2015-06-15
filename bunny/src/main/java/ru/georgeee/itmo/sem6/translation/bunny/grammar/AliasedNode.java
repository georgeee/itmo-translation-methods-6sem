package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

public class AliasedNode implements Node {
    private final Node node;
    @Getter
    private final String alias;

    @Override
    public String toString() {
        if (alias != null)
            return alias + ':' + node;
        return node.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AliasedNode aliasedNode = (AliasedNode) o;

        if (!node.equals(aliasedNode.node)) return false;

        return true;
    }

    @Override
    public String getId() {
        return node.getId();
    }

    @Override
    public int getNodeId() {
        return node.getNodeId();
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

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    public AliasedNode(Node node, String alias) {
        this.node = node;
        this.alias = alias;
    }
}
