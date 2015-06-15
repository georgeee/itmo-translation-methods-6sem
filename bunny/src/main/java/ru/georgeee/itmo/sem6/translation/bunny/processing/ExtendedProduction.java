package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Production;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class ExtendedProduction implements Iterable<ExtendedNode>{
    @Getter
    private final ExtendedNode parent;
    @Getter
    private final Production production;
    private final List<ExtendedNode> nodes;

    ExtendedProduction(ExtendedNode parent, Production production, List<ExtendedNode> nodes) {
        this.parent = parent;
        this.production = production;
        this.nodes = nodes;
    }

    public int size() {
        return nodes.size();
    }

    public ExtendedNode get(int index) {
        return nodes.get(index);
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public Iterator<ExtendedNode> iterator() {
        return nodes.iterator();
    }

    public void print(Appendable out) throws IOException {
        parent.print(out);
        out.append(" -> ");
        for(ExtendedNode node : nodes){
            node.print(out);
            out.append(' ');
        }
        out.append('\n');
    }
}
