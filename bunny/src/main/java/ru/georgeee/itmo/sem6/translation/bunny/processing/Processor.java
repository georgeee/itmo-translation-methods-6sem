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
    private final Map<IndexedProduction, ItemSet> allItems = new HashMap<>();
    private final ExtendedGrammar extendedGrammar;

    private ItemSet[][] transitionTable;

    private final Grammar grammar;
    private SetsComputer setsComputer;

    public Processor(Grammar grammar) {
        this.grammar = grammar;
        extendedGrammar = new ExtendedGrammar(grammar);
    }

    public void compute() {
        computeItemSets();
        computeTransitionTable();
        computeExtendedGrammar();
        setsComputer = extendedGrammar.createSetsComputer();
        setsComputer.compute();
    }

    private ItemSet createItemSet() {
        return new ItemSet(itemSets.size());
    }

    private void computeItemSets() {
        addItemSet(ItemSet.fillInitial(grammar.getStart(), createItemSet()));
        int i = 0;
        while (i < itemSets.size()) {
            ItemSet itemSet = itemSets.get(i);
            for (Map.Entry<Node, List<IndexedProduction>> goEntry : itemSet.getGoMap().entrySet()) {
                List<IndexedProduction> productions = goEntry.getValue();
                if (productions.isEmpty()) {
                    throw new IllegalArgumentException("Empty productions");
                } else if (!needNewItemSet(productions)) {
                    ItemSet newItemSet = createItemSet();
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

    private void computeTransitionTable() {
        transitionTable = new ItemSet[itemSets.size()][grammar.getNodeCount()];
        for (ItemSet from : itemSets) {
            for (Map.Entry<Node, List<IndexedProduction>> goEntry : from.getGoMap().entrySet()) {
                List<IndexedProduction> productions = goEntry.getValue();
                IndexedProduction someProduction = productions.get(0);
                Node node = goEntry.getKey();
                ItemSet to = allItems.get(someProduction.next());
                transitionTable[from.getId()][node.getNodeId()] = to;
            }
        }
    }

    private void computeExtendedGrammar() {
        for (ItemSet from : itemSets) {
            for (Map.Entry<Node, List<IndexedProduction>> goEntry : from.getGoMap().entrySet()) {
                List<IndexedProduction> productions = goEntry.getValue();
                IndexedProduction someProduction = productions.get(0);
                ItemSet to = allItems.get(someProduction.next());
                for (IndexedProduction iP : productions) {
                    if (iP.getIndex() == 0) {
                        extendedGrammar.add(createExtendedProduction(from, to, iP));
                    }
                }
            }
        }
    }

    private ExtendedProduction createExtendedProduction(ItemSet from, ItemSet to, IndexedProduction production) {
        List<ExtendedNode> nodes = new ArrayList<>();
        ItemSet currentFrom = from;
        for (Node node : production.getProduction()) {
            ItemSet currentTo = transitionTable[currentFrom.getId()][node.getNodeId()];
            ExtendedNode extNode = extendedGrammar.createNode(currentFrom, currentTo, node);
            nodes.add(extNode);
            currentFrom = currentTo;
        }
        Node parent = production.getProduction().getParent();
        ExtendedNode extParent = extendedGrammar.createNode(from, transitionTable[from.getId()][parent.getNodeId()], parent);
        return new ExtendedProduction(extParent, production.getProduction(), nodes);
    }

    private ItemSet go(ItemSet from, Node key) {
        return transitionTable[from.getId()][key.getNodeId()];
    }

    private boolean needNewItemSet(List<IndexedProduction> productions) {
        //if item set is present, then
        return allItems.containsKey(productions.get(0).next());
    }

    private void addItemSet(ItemSet itemSet) {
        for (IndexedProduction iP : itemSet) {
            ItemSet prev = allItems.put(iP, itemSet);
            if (prev != null) {
                log.warn("Duplicate production {}: replaced item set #{} with #{}", iP, prev, itemSet);
            }
        }
        itemSets.add(itemSet);
    }

    public void printItemSets(Appendable out) throws IOException {
        for (ItemSet itemSet : itemSets) {
            out.append("========= ").append(String.valueOf(itemSet.getId())).append(" ==========\n");
            itemSet.print(out);
        }
    }

    public void printExtendedGrammar(Appendable out) throws IOException{
        for(ExtendedProduction production : extendedGrammar){
            production.print(out);
        }
        out.append("-------- Sets --------");
        setsComputer.print(out);
    }


}
