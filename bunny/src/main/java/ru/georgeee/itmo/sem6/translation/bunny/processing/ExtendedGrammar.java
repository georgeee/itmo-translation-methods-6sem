package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Nonterminal;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Terminal;

import java.io.IOException;
import java.util.*;

class ExtendedGrammar implements Iterable<ExtendedProduction> {
    private final Grammar originalGrammar;
    private final List<ExtendedNode> nodes = new ArrayList<>();
    @Getter
    private final List<Boolean> nullable = new ArrayList<>();
    private final Map<Triple<Integer, Integer, Integer>, ExtendedNode> nodeMap = new HashMap<>();
    @Getter
    private final List<ExtendedProduction> productions = new ArrayList<>();
    @Getter
    private final List<Set<Terminal>> first = new ArrayList<>();
    @Getter
    private final List<Set<Terminal>> follow = new ArrayList<>();

    ExtendedGrammar(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    public boolean add(ExtendedProduction extendedProduction) {
        if (extendedProduction.isEmpty()) {
            nullable.set(extendedProduction.getParent().getId(), true);
        }
        return productions.add(extendedProduction);
    }

    @Override
    public Iterator<ExtendedProduction> iterator() {
        return productions.iterator();
    }

//    void computeNullable() {
//        boolean changed;
//        do {
//            changed = false;
//            for (ExtendedProduction production : productions) {
//                int parentId = production.getParent().getId();
//                if (!nullable.get(parentId)) {
//                    boolean allNullable = true;
//                    for (ExtendedNode node : production) {
//                        if (!nullable.get(node.getId())) {
//                            allNullable = false;
//                            break;
//                        }
//                    }
//                    if (allNullable) {
//                        nullable.set(parentId, true);
//                        changed = true;
//                    }
//                }
//            }
//        } while (changed);
//    }
//
//    void computeFirst() {
//        boolean changed;
//        do {
//            changed = false;
//            for (ExtendedProduction production : productions) {
//                for (ExtendedNode node : production) {
//                    changed |= first.get(production.getParent().getId()).addAll(first.get(node.getId()));
//                    if (!nullable.get(node.getId())) break;
//                }
//            }
//        } while (changed);
//    }
//

    void computeSets() {
        boolean changed;
        do {
            changed = false;
            for (ExtendedProduction production : productions) {
                int parentId = production.getParent().getId();
                if (!nullable.get(parentId) && allNullable(production)) {
                    changed = true;
                    nullable.set(parentId, true);
                }
                for (int i = 0; i < production.size(); ++i) {
                    ExtendedNode iNode = production.get(i);
                    int iId = iNode.getId();
                    if (allNullable(production, 0, i)) {
                        changed |= first.get(parentId).addAll(first.get(iId));
                    }
                    if (production.get(i).getNode() instanceof Nonterminal) {
                        for (int j = i + 1; j < production.size(); ++j) {
                            ExtendedNode jNode = production.get(j);
                            int jId = jNode.getId();
                            if (allNullable(production, i + 1, production.size())) {
                                changed |= follow.get(iId).addAll(follow.get(parentId));
                            }
                            if (allNullable(production, i + 1, j)) {
                                changed |= follow.get(iId).addAll(first.get(jId));
                            }
                        }
                    }
                }
            }
        } while (changed);
    }

    private boolean allNullable(ExtendedProduction production, int start, int end) {
        for (int i = start; i < end; ++i) {
            if (!nullable.get(production.get(i).getId())) {
                return false;
            }
        }
        return true;
    }

    private boolean allNullable(ExtendedProduction production) {
        return allNullable(production, 0, production.size());
    }


    public int size() {
        return productions.size();
    }

    public ExtendedNode createNode(ItemSet from, ItemSet to, Node node) {
        Triple<Integer, Integer, Integer> key = new ImmutableTriple<>(from.getId(), to == null ? -1 : to.getId(), node.getNodeId());
        return nodeMap.computeIfAbsent(key, k -> {
            ExtendedNode extNode = new ExtendedNode(nodes.size(), from, to, node);
            if (node instanceof Terminal) {
                first.add(Collections.singleton((Terminal) node));
                follow.add(null);
            } else {
                first.add(new HashSet<>());
                follow.add(new HashSet<>());
                if (originalGrammar.getStart().equals(node)) {
                    follow.get(extNode.getId()).add(null);
                }
            }
            nullable.add(false);
            nodes.add(extNode);
            return extNode;
        });
    }

    public void printSets(Appendable out) throws IOException {
        for (int i = 0; i < nodes.size(); ++i) {
            ExtendedNode node = nodes.get(i);
            out.append("============= Node ");
            node.print(out);
            out.append(" ============\n");
            out.append("nullable: ").append(String.valueOf(nullable.get(i))).append("\n");
            out.append("first: ").append(first.get(i).toString()).append("\n");
            if (node.getNode() instanceof Nonterminal) {
                out.append("follow: ").append(follow.get(i).toString()).append("\n");
            }
        }
    }
}
