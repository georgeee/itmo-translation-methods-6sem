package ru.georgeee.itmo.sem6.translation.bunny.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;

import java.io.IOException;
import java.util.*;

public class Processor {
    private static final Logger log = LoggerFactory.getLogger(Processor.class);
    private final List<ItemSet> itemSets = new ArrayList<>();
    private final Set<IndexedProduction> allItems = new HashSet<>();

    private final Grammar grammar;

    public Processor(Grammar grammar) {
        this.grammar = grammar;
    }

    public void computeItemSets() {
        addItemSet(ItemSet.createInitial(grammar.getStart()));
        int i = 0;
        while (i < itemSets.size()) {
            ItemSet itemSet = itemSets.get(i);
            for (Map.Entry<Node, List<IndexedProduction>> goEntry : itemSet.getGoMap().entrySet()) {
                List<IndexedProduction> productions = goEntry.getValue();
                if (productions.isEmpty()) {
                    throw new IllegalArgumentException("Empty productions");
                } else if (!needNewItemSet(productions)) {
                    ItemSet newItemSet = new ItemSet(itemSets.size() + 1);
                    for (IndexedProduction production : productions) {
                        newItemSet.add(production.next(), false);
                    }
                    newItemSet.completeToClosure(new HashSet<Nonterminal>());
                    addItemSet(newItemSet);
                }
            }
            ++i;
        }
    }

    private boolean needNewItemSet(List<IndexedProduction> productions) {
        //if item set is present, then
        return allItems.contains(productions.get(0).next());
    }

    private void addItemSet(ItemSet itemSet) {
        for (IndexedProduction iP : itemSet) {
            if (!allItems.add(iP)) {
                log.warn("Duplicate production: {}", iP);
            }
        }
        itemSets.add(itemSet);
    }

    public void printItemSets(Appendable out) throws IOException {
        int i = 0;
        for (ItemSet itemSet : itemSets) {
            out.append("========= ").append(String.valueOf(i++)).append(" ==========\n");
            itemSet.print(out);
        }
    }


}
