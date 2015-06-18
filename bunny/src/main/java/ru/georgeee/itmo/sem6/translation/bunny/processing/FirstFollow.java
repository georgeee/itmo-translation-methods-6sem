package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.IProduction;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Terminal;

import java.io.IOException;
import java.util.*;

public class FirstFollow {
    private final List<? extends IProduction<? extends Node>> productions;
    private final List<? extends Node> nodes;
    private final Node start;
    @Getter
    private final List<Boolean> nullable = new ArrayList<>();
    @Getter
    private final List<Set<Terminal>> first = new ArrayList<>();
    @Getter
    private final List<Set<Terminal>> follow = new ArrayList<>();

    public FirstFollow(List<? extends IProduction<? extends Node>> productions, List<? extends Node> nodes, Node start) {
        this.productions = productions;
        this.nodes = nodes;
        this.start = start;
    }

    public void compute() {
        init();
        computeSets();
    }

    private void init() {
        for (Node node : nodes) {
            if (node.isTerminal()) {
                Set<Terminal> initFirst = new HashSet<>();
                initFirst.add((Terminal) node.unwrap());
                first.add(initFirst);
                follow.add(null);
            } else {
                first.add(new HashSet<>());
                follow.add(new HashSet<>());
                if (start.unwrap().equals(node.unwrap())) {
                    follow.get(node.getNodeId()).add(null);
                }
            }
            nullable.add(false);
        }
        for (IProduction production : productions) {
            if (production.isEmpty()) {
                nullable.set(production.getParent().getNodeId(), true);
            }
        }
    }

    private void computeSets() {
        boolean changed;
        //first and nullable
        do {
            changed = false;
            for (IProduction production : productions) {
                int parentId = production.getParent().getNodeId();
                if (!nullable.get(parentId) && allNullable(production)) {
                    changed = true;
                    nullable.set(parentId, true);
                }
                for (int i = 0; i < production.size(); ++i) {
                    Node iNode = production.get(i);
                    int iId = iNode.getNodeId();
                    if (allNullable(production, 0, i)) {
                        changed |= first.get(parentId).addAll(first.get(iId));
                    }
                }
            }
        } while (changed);
        //follow
        do {
            changed = false;
            for (IProduction production : productions) {
                int parentId = production.getParent().getNodeId();
                for (int i = 0; i < production.size(); ++i) {
                    Node iNode = production.get(i);
                    if (iNode.isNonterminal()) {
                        int iId = iNode.getNodeId();
                        for (int j = i + 1; j < production.size(); ++j) {
                            Node jNode = production.get(j);
                            int jId = jNode.getNodeId();
                            changed |= follow.get(iId).addAll(first.get(jId));
                            if (!nullable.get(jId)) break;
                        }
                        if (allNullable(production, i + 1)) {
                            changed |= follow.get(iId).addAll(follow.get(parentId));
                        }
                    }
                }
            }
        } while (changed);
    }

    private boolean allNullable(IProduction production, int start, int end) {
        for (int i = start; i < end; ++i) {
            if (!nullable.get(production.get(i).getNodeId())) {
                return false;
            }
        }
        return true;
    }

    private boolean allNullable(IProduction production, int start) {
        return allNullable(production, start, production.size());
    }

    private boolean allNullable(IProduction production) {
        return allNullable(production, 0);
    }

    public void print(Appendable out) throws IOException {
        for (int i = 0; i < nodes.size(); ++i) {
            Node node = nodes.get(i);
            out.append("--> ").append(node.toString()).append(" \n");
            out.append("nullable: ").append(String.valueOf(nullable.get(i))).append("\n");
            out.append("first: ").append(first.get(i).toString()).append("\n");
            if (node.isNonterminal()) {
                out.append("follow: ").append(follow.get(i).toString()).append("\n");
            }
        }
    }

    public Set<Terminal> getFirst(Node node) {
        return first.get(node.getNodeId());
    }
    public Set<Terminal> getFirst(Iterable<? extends Node> nodes) {
        Set<Terminal> result = new HashSet<>();
        for(Node node : nodes){
            result.addAll(getFirst(node));
            if(!isNullable(node)){
                break;
            }
        }
        return result;
    }

    public boolean isNullable(Iterable<?extends Node> nodes) {
        for(Node node : nodes){
            if(!isNullable(node)){
                return false;
            }
        }
        return true;
    }
    public boolean isNullable(Node node) {
        return nullable.get(node.getNodeId());
    }
}
