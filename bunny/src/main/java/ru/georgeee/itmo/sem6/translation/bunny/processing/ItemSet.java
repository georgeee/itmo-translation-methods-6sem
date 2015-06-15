package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Production;
import ru.georgeee.itmo.sem6.translation.bunny.utils.Utils;

import java.io.IOException;
import java.util.*;

class ItemSet implements Iterable<IndexedProduction> {
    @Getter
    private final int id;
    private final List<IndexedProduction> items = new ArrayList<>();

    @Getter
    private final Map<Node, List<IndexedProduction>> goMap = new HashMap<>();

    public ItemSet(int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    @Override
    public Iterator<IndexedProduction> iterator() {
        return items.iterator();
    }

    public IndexedProduction get(int index) {
        return items.get(index);
    }

    public void add(IndexedProduction iP, boolean synthetic) {
        int pos = iP.getIndex();
        if (iP.hasNext()) {
            Node node = iP.getProduction().get(pos).getNode();
            Utils.addToMap(goMap, node, iP);
        }
        if (!synthetic) {
            items.add(iP);
        }
    }

    public static ItemSet fillInitial(Nonterminal start, ItemSet itemSet) {
        for (Production production : start) {
            itemSet.add(new IndexedProduction(production, 0), false);
        }
        Set<Nonterminal> used = new HashSet<>();
        used.add(start);
        itemSet.completeToClosure(used);
        return itemSet;
    }

    public void completeToClosure(Set<Nonterminal> used) {
        Queue<IndexedProduction> queue = new ArrayDeque<>();
        queue.addAll(items);
        while (!queue.isEmpty()) {
            IndexedProduction iP = queue.remove();
            Production production = iP.getProduction();
            if (iP.hasNext()) {
                Node first = production.get(iP.getIndex()).getNode();
                if (first instanceof Nonterminal) {
                    if (!used.contains(first)) {
                        used.add((Nonterminal) first);
                        for (Production synProduction : (Nonterminal) first) {
                            IndexedProduction synIP = new IndexedProduction(synProduction, 0);
                            add(synIP, true);
                            queue.add(synIP);
                        }
                    }
                }
            }
        }
    }

    public void print(Appendable out) throws IOException {
        out.append("Items set for prefix");
        out.append('\n');
        printItems(items, out);
        out.append("--------------------\nGoto map:\n");
        for (Map.Entry<Node, List<IndexedProduction>> goEntry : goMap.entrySet()) {
            out.append("node: ").append(goEntry.getKey().getId()).append('\n');
            printItems(goEntry.getValue(), out);
            out.append("------------\n");
        }
    }

    private static void printItems(List<IndexedProduction> items, Appendable out) throws IOException {
        for (IndexedProduction iP : items) {
            out.append(iP.getProduction().getParent().getId()).append(" -> ");
            for (int i = 0; i < iP.getProduction().size(); ++i) {
                if (i == iP.getIndex()) {
                    out.append("• ");
                }
                out.append(iP.getProduction().get(i).getNode().getId()).append(' ');
            }
            if (iP.getIndex() == iP.getProduction().size()) {
                out.append("• ");
            }
            out.append('\n');
        }
    }
}
