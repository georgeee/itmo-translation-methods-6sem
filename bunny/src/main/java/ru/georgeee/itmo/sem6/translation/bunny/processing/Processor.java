package ru.georgeee.itmo.sem6.translation.bunny.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Terminal;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.Action;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.TableHolder;

import java.io.IOException;
import java.util.*;

public class Processor {
    private static final Logger log = LoggerFactory.getLogger(Processor.class);
    private final Map<ItemSet, ItemSet> itemsetMap = new HashMap<>();
    private final List<ItemSet> itemsets = new ArrayList<>();
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

    public void printTransitions(Appendable out) throws IOException {
        for (int i = 0; i < itemsets.size(); ++i) {
            for (Node node : grammar.getNodes()) {
                ItemSet to = transitionTable[i][node.getNodeId()];
                out.append(String.valueOf(i)).append(" + ")
                        .append(node.toString()).append(" -> ")
                        .append(String.valueOf(to == null ? null : to.getId()))
                        .append(String.valueOf('\n'));
            }
        }
    }

    public TableHolder createTableHolder() {
        return new TableHolder(actionTable, actionTypeTable, gotoTable);
    }

    public void computeActionGotoTables() {
        actionTable = new int[itemsets.size()][grammar.getTerminals().size() + 1];
        actionTypeTable = new Action[itemsets.size()][grammar.getTerminals().size() + 1];
        gotoTable = new int[itemsets.size()][grammar.getNonterminals().size()];
        for (int i = 0; i < itemsets.size(); ++i) {
            if (containsFinishingProduction(itemsets.get(i))) {
                actionTypeTable[i][0] = Action.ACCEPT;
            }
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

    private void computeItemSets() {
        addItemSet(ItemSet.createInitial(grammar.getStart()));
        int i = 0;
        while (i < itemsets.size()) {
            ItemSet itemSet = itemsets.get(i);
            for (Map.Entry<Node, Set<IndexedProduction>> goEntry : itemSet.getGoMap().entrySet()) {
                Set<IndexedProduction> productions = goEntry.getValue();
                if (productions.isEmpty()) {
                    throw new IllegalArgumentException("Empty productions");
                } else {
                    ItemSet newItemSet = new ItemSet(iterate(productions));
                    newItemSet.completeToClosure();
                    addItemSet(newItemSet);
                }
            }
            ++i;
        }
    }

    private Set<IndexedProduction> iterate(Collection<IndexedProduction> productions) {
        Set<IndexedProduction> set = new HashSet<>();
        for (IndexedProduction production : productions) {
            if (!production.hasNext()) {
                log.error("Can't iterate {}", production);
            }
            set.add(production.next());
        }
        return set;
    }

    private void computeTransitionTable() {
        transitionTable = new ItemSet[itemsets.size()][grammar.getNodeCount()];
        for (ItemSet from : itemsets) {
            for (Map.Entry<Node, Set<IndexedProduction>> goEntry : from.getGoMap().entrySet()) {
                ItemSet itemSet = new ItemSet(iterate(goEntry.getValue()));
                itemSet.completeToClosure();
                Node node = goEntry.getKey();
                ItemSet to = itemsetMap.get(itemSet);
                if (to == null) {
                    log.error("Null destination for {}, {}", from.getId(), node);
                }
                transitionTable[from.getId()][node.getNodeId()] = to;
            }
        }
    }

    private void computeExtendedGrammar() {
        for (ItemSet from : itemsets) {
            for (Map.Entry<Node, Set<IndexedProduction>> goEntry : from.getGoMap().entrySet()) {
                Set<IndexedProduction> productions = goEntry.getValue();
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

    private void addItemSet(ItemSet itemSet) {
        if (!itemsetMap.containsKey(itemSet)) {
            itemSet.setId(itemsets.size());
            itemsetMap.put(itemSet, itemSet);
            itemsets.add(itemSet);
        }
    }

    public void printItemSets(Appendable out) throws IOException {
        for (ItemSet itemSet : itemsets) {
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
        for (int i = 0; i < itemsets.size(); ++i) {
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

}
