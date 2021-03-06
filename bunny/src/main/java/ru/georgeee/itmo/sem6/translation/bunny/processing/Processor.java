package ru.georgeee.itmo.sem6.translation.bunny.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.*;
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
    private FirstFollow firstFollow;

    private int[][] actionTable;
    private Action[][] actionTypeTable;
    private int[][] gotoTable;

    public Processor(Grammar grammar) {
        this.grammar = grammar;
        extendedGrammar = new ExtendedGrammar(grammar);
    }

    public void compute() {
        firstFollow = grammar.createFirstFollow();
        firstFollow.compute();
        computeItemSets();
        computeTransitionTable();
        computeExtendedGrammar();
        computeActionGotoTables();
    }

    public void printTransitions(Appendable out) throws IOException {
        for (int i = 0; i < itemsets.size(); ++i) {
            for (Node node : grammar.getNodes()) {
                ItemSet to = transitionTable[i][node.getNodeId()];
                if(to == null){
                    continue;
                }
                out.append(String.valueOf(i)).append(" + ")
                        .append(node.toString()).append(" -> ")
                        .append(String.valueOf(to.getId()))
                        .append(String.valueOf('\n'));
            }
        }
    }

    public TableHolder createTableHolder() {
        return new TableHolder(actionTable, actionTypeTable, gotoTable);
    }

    private void computeActionGotoTables() {
        actionTable = new int[itemsets.size()][grammar.getTerminals().size() + 1];
        actionTypeTable = new Action[itemsets.size()][grammar.getTerminals().size() + 1];
        gotoTable = new int[itemsets.size()][grammar.getNonterminals().size()];
        for (int i = 0; i < itemsets.size(); ++i) {
            if (containsFinishingProduction(itemsets.get(i))) {
                setAction(i, 0, Action.ACCEPT, 0);
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
                    if (to != null) {
                        setAction(i, termId, Action.SHIFT, toId);
                    }
                }
            }
        }
        for (ExtendedProduction production : extendedGrammar) {
            if (grammar.isStart(production.getParent())) {
                continue;
            }
            ItemSet finalSet = production.getFinalSet();
            int finalSetId = finalSet.getId();
            int productionId = production.getId();
            Terminal terminal = production.getProduction().getTerminal();
            int termId = terminal == null ? 0 : 1 + terminal.getTermId();
            setAction(finalSetId, termId, Action.REDUCE, productionId);
        }
    }

    private void setAction(int finalSetId, int termId, Action action, int productionId) {
        if (actionTypeTable[finalSetId][termId] != null
                && !(actionTypeTable[finalSetId][termId] == action && actionTable[finalSetId][termId] == productionId)) {
            log.warn("Overwriting action {}:{} with {}:{} for setId={} termId={}",
                    actionTypeTable[finalSetId][termId], actionTable[finalSetId][termId],
                    action, productionId, finalSetId, termId);
        }
        actionTypeTable[finalSetId][termId] = action;
        actionTable[finalSetId][termId] = productionId;
    }

    private boolean containsFinishingProduction(ItemSet itemSet) {
        for (IndexedProduction production : itemSet) {
            if (!production.hasNext()
                    && grammar.isStart(production.getParent())
                    && production.getTerminal() == null) {
                return true;
            }
        }
        return false;
    }

    private void computeItemSets() {
        addItemSet(ItemSet.createInitial(grammar.getStart(), firstFollow));
        int i = 0;
        while (i < itemsets.size()) {
            ItemSet itemSet = itemsets.get(i);
            for (Map.Entry<Node, Set<IndexedProduction>> goEntry : itemSet.getGoMap().entrySet()) {
                Set<IndexedProduction> productions = goEntry.getValue();
                if (productions.isEmpty()) {
                    throw new IllegalArgumentException("Empty productions");
                } else {
                    ItemSet newItemSet = new ItemSet(iterate(productions), firstFollow);
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
                ItemSet itemSet = new ItemSet(iterate(goEntry.getValue()), firstFollow);
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
            for (IndexedProduction iP : from) {
                if (iP.getIndex() == 0) {
                    extendedGrammar.add(createExtendedProduction(from, iP));
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
        return new ExtendedProduction(extParent, production, nodes);
    }

    private ItemSet go(ItemSet from, Node key) {
        return transitionTable[from.getId()][key.getNodeId()];
    }

    private void addItemSet(ItemSet itemSet) {
        if (!itemsetMap.containsKey(itemSet)) {
            log.debug("Added item set: {}", itemSet.getItems());
            itemSet.setId(itemsets.size());
            itemsetMap.put(itemSet, itemSet);
            itemsets.add(itemSet);
        } else {
            log.debug("Rejecting {}", itemSet.getItems());
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
//        extFirstFollow.print(out);
    }

    public void printActionGotoTablesCsv(Appendable out) throws IOException {
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
                            out.append("r").append(String.valueOf(actionTable[i][j])).append(")");
                            break;
                        case SHIFT:
                            out.append("s").append(String.valueOf(actionTable[i][j]));
                            break;
                        case ACCEPT:
                            out.append("accept");
                            break;
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
        out.append("\nRules list:\n");
        for (Production production : grammar) {
            out.append(String.valueOf(production.getId())).append(tableDelim).append(production.toString()).append('\n');
        }
    }

}
