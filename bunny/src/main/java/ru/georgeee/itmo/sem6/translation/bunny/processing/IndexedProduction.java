package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.*;

import java.util.Iterator;
import java.util.Objects;

class IndexedProduction implements IProduction<AliasedNode> {
    @Getter
    private final Production production;
    @Getter
    private final int index;
    @Getter
    private final Terminal terminal;

    public IndexedProduction(Production production, int index, Terminal terminal) {
        if (index > production.size()) {
            throw new IllegalArgumentException("Can't create indexed production: index=" + index + " production=" + production);
        }
        this.production = production;
        this.index = index;
        this.terminal = terminal;
    }


    public IndexedProduction next() {
        return new IndexedProduction(production, index + 1, terminal);
    }

    @Override
    public AliasedNode get(int index) {
        return production.get(index);
    }

    @Override
    public Nonterminal getParent() {
        return production.getParent();
    }

    @Override
    public int size() {
        return production.size();
    }

    @Override
    public boolean isEmpty() {
        return production.isEmpty();
    }

    @Override
    public Iterator<AliasedNode> iterator() {
        return production.iterator();
    }

    public Node nextNode() {
        return production.get(index).unwrap();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getParent()).append(" -> ");
        for (int i = 0; i < production.size(); ++i) {
            if (i == index) {
                sb.append(" • ");
            }
            sb.append(production.get(i));
        }
        if (index == production.size()) {
            sb.append(" • ");
        }
        sb.append("; ").append(terminal);
        return sb.toString();
    }

    public boolean hasNext() {
        return index < production.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndexedProduction)) return false;
        IndexedProduction that = (IndexedProduction) o;
        return Objects.equals(index, that.index) &&
                Objects.equals(production, that.production) &&
                Objects.equals(terminal, that.terminal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(production, index, terminal);
    }
}
