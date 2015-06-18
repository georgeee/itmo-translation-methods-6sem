package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.IProduction;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class ExtendedProduction implements IProduction<ExtendedNode> {
    @Getter
    private final ExtendedNode parent;
    @Getter
    private final IndexedProduction production;
    private final List<ExtendedNode> nodes;

    ExtendedProduction(ExtendedNode parent, IndexedProduction production, List<ExtendedNode> nodes) {
        this.parent = parent;
        this.production = production;
        this.nodes = nodes;
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public ExtendedNode get(int index) {
        return nodes.get(index);
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public Iterator<ExtendedNode> iterator() {
        return nodes.iterator();
    }

    public void print(Appendable out) throws IOException {
        out.append(parent.toString()).append(" -> ");
        for (ExtendedNode node : nodes) {
            out.append(node.toString()).append(' ');
        }
        out.append('\n');
    }

    public ItemSet getFinalSet() {
        return nodes.get(size() - 1).getTo();
    }

    public int getId() {
        return getProduction().getProduction().getId();
    }
}
