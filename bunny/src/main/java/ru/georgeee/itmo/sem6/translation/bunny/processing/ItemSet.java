package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Production;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Terminal;

import java.io.IOException;
import java.util.*;

class ItemSet implements Iterable<IndexedProduction> {
    private static final Logger log = LoggerFactory.getLogger(Processor.class);
    @Getter
    @Setter
    private int id;
    @Getter
    private final Set<IndexedProduction> items = new HashSet<>();

    @Getter
    private final Map<Node, Set<IndexedProduction>> goMap = new HashMap<>();

    private final FirstFollow firstFollow;

    public ItemSet(FirstFollow firstFollow) {
        this.firstFollow = firstFollow;
    }

    public ItemSet(Collection<IndexedProduction> productions, FirstFollow firstFollow) {
        this.firstFollow = firstFollow;
        addAll(productions);
    }

    @Override
    public Iterator<IndexedProduction> iterator() {
        return items.iterator();
    }

    public void addAll(Iterable<IndexedProduction> productions) {
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

    public static ItemSet createInitial(Nonterminal start, FirstFollow firstFollow) {
        ItemSet itemSet = new ItemSet(firstFollow);
        for (Production production : start) {
            itemSet.add(new IndexedProduction(production, 0, null));
        }
        itemSet.completeToClosure();
        return itemSet;
    }

    public void completeToClosure() {
        Queue<IndexedProduction> queue = new ArrayDeque<>();
        queue.addAll(items);
//        log.debug("complete to closure: {}", getItems());
//        log.debug("init queue: {}", queue);
        Set<IndexedProduction> used = new HashSet<>();
        while (!queue.isEmpty()) {
            IndexedProduction iP = queue.remove();
//            log.debug("Took from queue: {}, {}", iP, iP.hasNext());
            if (iP.hasNext()) {
                Node first = iP.get(iP.getIndex()).unwrap();
                if (first instanceof Nonterminal) {
//                    log.debug("for production {}", iP);
                    for (Production synProduction : (Nonterminal) first) {
                        for (Terminal terminal : getFirstSet(iP)) {
                            IndexedProduction synIP = new IndexedProduction(synProduction, 0, terminal);
                            if(!used.contains(synIP)){
                                used.add(synIP);
                                add(synIP);
                                queue.add(synIP);
                            }
//                            log.debug("generating production: {}", synIP);
                        }
                    }
                }
            }
        }
    }


    private Set<Terminal> getFirstSet(IndexedProduction iP) {
        List<? extends Node> nodes = iP.getProduction().getNodes().subList(iP.getIndex() + 1, iP.getProduction().size());
        Set<Terminal> set = firstFollow.getFirst(nodes);
        if (firstFollow.isNullable(nodes)) {
            set.add(iP.getTerminal());
        }
        return set;
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
        out.append("======= Goto map =====\n");
        for (Map.Entry<Node, Set<IndexedProduction>> entry : goMap.entrySet()) {
            printItems(entry.getValue(), out, "  " + entry.getKey().toString() + ": ");
        }
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
            out.append(" ; ").append(String.valueOf(iP.getTerminal())).append('\n');
        }
    }
}
