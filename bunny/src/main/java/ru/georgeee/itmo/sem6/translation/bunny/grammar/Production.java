package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Production implements IProduction<AliasedNode> {
    @Getter
    private final Nonterminal parent;
    @Getter
    private final int id;
    private final List<AliasedNode> nodes;
    @Getter
    private final String codeBlock;

    Production(Nonterminal parent, int id, List<AliasedNode> nodes, String codeBlock) {
        this.parent = parent;
        this.id = id;
        this.nodes = nodes;
        this.codeBlock = codeBlock;
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public Iterator<AliasedNode> iterator() {
        return nodes.iterator();
    }

    @Override
    public AliasedNode get(int index) {
        return nodes.get(index);
    }

    @Override
    public String toString() {
        return "Production{" +
                "parent=" + parent.getId() +
                ", nodes=" + nodes +
                '}';
    }

    public void print(Appendable out) throws IOException {
        out.append(parent.toString()).append(" -> ");
        for (Node node : nodes) {
            out.append(node.toString()).append(' ');
        }
        out.append('\n');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production that = (Production) o;

        if (id != that.id) return false;

        return this == o;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
