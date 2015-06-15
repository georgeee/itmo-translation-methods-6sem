package ru.georgeee.itmo.sem6.translation.bunny.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Terminal;

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

    private int[][] actionTable;
    private Action[][] actionTypeTable;
    private int[][] gotoTable;

    public Processor(Grammar grammar) {
        this.grammar = grammar;
        extendedGrammar = new ExtendedGrammar(grammar);
    }

    public void compute() {
        computeItemSets();
        computeTransitionTable();
        computeExtendedGrammar();
        computeActionGotoTables();
    }

    public void computeActionGotoTables() {
        actionTable = new int[itemSets.size()][grammar.getTerminals().size() + 1];
        actionTypeTable = new Action[itemSets.size()][grammar.getTerminals().size() + 1];
        gotoTable = new int[itemSets.size()][grammar.getNonterminals().size()];
        for (int i = 0; i < itemSets.size(); ++i) {
//            if (containsFinishingProduction(itemSets.get(i))) {
//                actionTypeTable[i][0] = Action.ACCEPT;
//            }
            for (Node node : grammar.getNodes()) {
                ItemSet to = transitionTable[i][node.getNodeId()];
                int toId = to == null ? -1 : to.getId();
                if (node.isNonterminal()) {
                    Nonterminal nonterminal = (Nonterminal) node.unwrap();
                    gotoTable[i][nonterminal.getNontermId()] = toId;
                } else {
                    Terminal terminal = (Terminal) node.unwrap();
                    int termId = terminal.getTermId() + 1;
                    actionTable[i][termId] = toId;
                    if (to != null) {
                        actionTypeTable[i][termId] = Action.SHIFT;
                    }
                }
            }
        }
        setsComputer = extendedGrammar.createSetsComputer();
        setsComputer.compute();
        for (ExtendedProduction production : extendedGrammar) {
            ItemSet finalSet = production.getFinalSet();
            int finalSetId = finalSet.getId();
            int productionId = production.getProduction().getId();
            Set<Terminal> follow = setsComputer.getFollow().get(production.getParent().getNodeId());
            for (Terminal terminal : follow) {
                int termId = terminal == null ? 0 : terminal.getTermId() + 1;
                int prev = actionTable[finalSetId][termId];
                if (prev != productionId) {
                    if (prev != -1 && actionTypeTable[finalSetId][termId] == Action.REDUCE) {
                        log.warn("Duplicate candidate (reduce) for final set {}, terminal {}: candidate={} prev={}", finalSet.getId(), terminal, production.getProduction(), grammar.get(prev));
//                        log.warn("Tried to replace {} {}, with reduce: finalSet={} terminal={} candidate={}", actionTypeTable[finalSetId][termId], actionTable[finalSetId][termId], finalSet.getId(), terminal, production.getProduction());
                    } else {
                        actionTypeTable[finalSetId][termId] = Action.REDUCE;
                        actionTable[finalSetId][termId] = productionId;
                    }
                }
            }
        }
    }

    private boolean containsFinishingProduction(ItemSet itemSet) {
        for (IndexedProduction production : itemSet) {
            if (!production.hasNext() && production.getParent().unwrap() == grammar.getStart().unwrap()) {
                return true;
            }
        }
        return false;
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
                for (IndexedProduction iP : productions) {
                    if (iP.getIndex() == 0) {
                        extendedGrammar.add(createExtendedProduction(from, iP));
                    }
                }
            }
        }
    }

    private ExtendedProduction createExtendedProduction(ItemSet from, IndexedProduction production) {
        List<ExtendedNode> nodes = new ArrayList<>();
        ItemSet currentFrom = from;
        for (Node node : production) {
            ItemSet currentTo = transitionTable[currentFrom.getId()][node.getNodeId()];
            ExtendedNode extNode = extendedGrammar.createNode(currentFrom, currentTo, node);
            nodes.add(extNode);
            currentFrom = currentTo;
        }
        Node parent = production.getParent();
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

    public void printExtendedGrammar(Appendable out) throws IOException {
        for (ExtendedProduction production : extendedGrammar) {
            production.print(out);
        }
        out.append("-------- Sets --------\n");
        setsComputer.print(out);
    }

    public void printActionGotoTables(Appendable out) throws IOException {
        String tableDelim = ";";
        out.append("Set").append(tableDelim).append("$").append(tableDelim);
        for (Node node : grammar.getTerminals()) {
            out.append(node.toString()).append(tableDelim);
        }
        for (Node node : grammar.getNonterminals()) {
            out.append(node.toString()).append(tableDelim);
        }
        out.append('\n');
        for (int i = 0; i < itemSets.size(); ++i) {
            out.append(String.valueOf(i)).append(tableDelim);
            for (int j = 0; j < grammar.getTerminals().size() + 1; ++j) {
                if (actionTypeTable[i][j] != null) {
                    switch (actionTypeTable[i][j]) {
                        case REDUCE:
                            out.append("r(").append(grammar.get(actionTable[i][j]).toString()).append(")");
                            break;
                        case SHIFT:
                            out.append("s").append(String.valueOf(actionTable[i][j]));
                            break;
//                        case ACCEPT:
//                            out.append("a");
//                            break;
                    }
                }
                out.append(tableDelim);
            }
            //Goto table
            for (int j = 0; j < grammar.getNonterminals().size(); ++j) {
                out.append(gotoTable[i][j] == -1 ? "" : String.valueOf(gotoTable[i][j])).append(tableDelim);
            }
            out.append('\n');
        }

    }

    public static enum Action {
        REDUCE, SHIFT
//        , ACCEPT
    }
}
