package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Node;

import java.util.*;

class ExtendedGrammar implements Iterable<ExtendedProduction> {
    private final Grammar originalGrammar;
    private final List<ExtendedNode> nodes = new ArrayList<>();
    private final Map<Triple<Integer, Integer, Integer>, ExtendedNode> nodeMap = new HashMap<>();
    @Getter
    private final List<ExtendedProduction> productions = new ArrayList<>();

    ExtendedGrammar(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    public boolean add(ExtendedProduction extendedProduction) {
        return productions.add(extendedProduction);
    }

    @Override
    public Iterator<ExtendedProduction> iterator() {
        return productions.iterator();
    }

    public FirstFollow createFirstFollow(){
        return new FirstFollow(productions, nodes, originalGrammar.getStart());
    }

    public int size() {
        return productions.size();
    }

    public ExtendedNode createNode(ItemSet from, ItemSet to, Node node) {
        Triple<Integer, Integer, Integer> key = new ImmutableTriple<>(from.getId(), to == null ? -1 : to.getId(), node.getNodeId());
        return nodeMap.computeIfAbsent(key, k -> {
            ExtendedNode extNode = new ExtendedNode(nodes.size(), from, to, node);
            nodes.add(extNode);
            return extNode;
        });
    }

}
