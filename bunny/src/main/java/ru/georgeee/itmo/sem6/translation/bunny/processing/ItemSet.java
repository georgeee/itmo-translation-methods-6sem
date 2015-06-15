package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import lombok.Setter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Production;

import java.io.IOException;
import java.util.*;

class ItemSet implements Iterable<IndexedProduction> {
    @Getter @Setter
    private int id;
    @Getter
    private final Set<IndexedProduction> items = new HashSet<>();

    @Getter
    private final Map<Node, Set<IndexedProduction>> goMap = new HashMap<>();

    public ItemSet() {
    }

    public ItemSet(Collection<IndexedProduction> productions) {
        addAll(productions, false);
    }

    @Override
    public Iterator<IndexedProduction> iterator() {
        return items.iterator();
    }

    public void addAll(Iterable<IndexedProduction> productions, boolean synthetic) {
        for (IndexedProduction production : productions) {
            add(production);
        }
    }

    public void add(IndexedProduction iP) {
        int pos = iP.getIndex();
        if (iP.hasNext()) {
            Node node = iP.get(pos).unwrap();
            addToMap(goMap, node, iP);
        }
            items.add(iP);
    }

    private static <K, V> void addToMap(Map<K, Set<V>> map, K key, V value) {
        Set<V> set = map.get(key);
        if (set == null) {
            map.put(key, set = new HashSet<>());
        }
        set.add(value);
    }
    public static ItemSet createInitial(Nonterminal start) {
        ItemSet itemSet = new ItemSet();
        for (Production production : start) {
            itemSet.add(new IndexedProduction(production, 0));
        }
        Set<Nonterminal> used = new HashSet<>();
        used.add(start);
        itemSet.completeToClosure(used);
        return itemSet;
    }

    public void completeToClosure() {
        completeToClosure(new HashSet<>());
    }
    public void completeToClosure(Set<Nonterminal> used) {
        Queue<IndexedProduction> queue = new ArrayDeque<>();
        queue.addAll(items);
        while (!queue.isEmpty()) {
            IndexedProduction iP = queue.remove();
            if (iP.hasNext()) {
                Node first = iP.get(iP.getIndex()).unwrap();
                if (first instanceof Nonterminal) {
                    if (!used.contains(first)) {
                        used.add((Nonterminal) first);
                        for (Production synProduction : (Nonterminal) first) {
                            IndexedProduction synIP = new IndexedProduction(synProduction, 0);
                            add(synIP);
                            queue.add(synIP);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemSet itemSet = (ItemSet) o;

        if (!items.equals(itemSet.items)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public void print(Appendable out) throws IOException {
        printItems(items, out, "");
    }

    private static void printItems(Iterable<IndexedProduction> items, Appendable out, String prefix) throws IOException {
        for (IndexedProduction iP : items) {
            out.append(prefix).append(iP.getParent().getId()).append(" -> ");
            for (int i = 0; i < iP.size(); ++i) {
                if (i == iP.getIndex()) {
                    out.append("• ");
                }
                out.append(iP.get(i).getId()).append(' ');
            }
            if (iP.getIndex() == iP.size()) {
                out.append("• ");
            }
            out.append('\n');
        }
    }
}
