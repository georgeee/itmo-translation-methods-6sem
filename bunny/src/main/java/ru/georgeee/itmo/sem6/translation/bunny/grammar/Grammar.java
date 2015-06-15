package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import lombok.Setter;
import ru.georgeee.itmo.sem6.translation.bunny.processing.SetsComputer;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class Grammar implements Iterable<Production> {
    private final Map<String, Terminal> terminalMap = new HashMap<>();
    private final Map<String, Nonterminal> nonterminalMap = new HashMap<>();
    private final List<Production> productions = new ArrayList<>();
    @Getter
    private Nonterminal start;
    @Getter
    @Setter
    private String packageName;
    @Getter
    @Setter
    private String className;
    @Getter
    @Setter
    private String headerCodeBlock;
    @Getter
    private final List<Nonterminal> nonterminals = new ArrayList<>();
    @Getter
    private final List<Terminal> terminals = new ArrayList<>();
    @Getter
    private final List<Node> nodes = new ArrayList<>();

    public void addHeaderCodeBlock(String block) {
        headerCodeBlock = headerCodeBlock == null ? block : headerCodeBlock + block;
    }

    public void setStart(String start) {
        this.start = getOrCreateNonterminal(start);
    }

    private <N extends Node> N getOrCreateNode(String id, Map<String, N> map, List<N> list, BiFunction<Integer, Integer, N> factory) {
        N node;
        if (!map.containsKey(id)) {
            node = factory.apply(nodes.size(), list.size());
            nodes.add(node);
            list.add(node);
            map.put(id, node);
        } else {
            node = map.get(id);
        }
        return node;
    }

    public Production get(int index) {
        return productions.get(index);
    }

    public boolean isEmpty() {
        return productions.isEmpty();
    }

    public int size() {
        return productions.size();
    }

    public SetsComputer createSetsComputer() {
        return new SetsComputer(productions, nodes, start);
    }

    @Override
    public Iterator<Production> iterator() {
        return productions.iterator();
    }

    public Nonterminal getOrCreateNonterminal(String id) {
        return getOrCreateNode(id, nonterminalMap, nonterminals, (nodeId, nonTermId) -> new Nonterminal(this, id, nodeId, nonTermId));
    }

    public Terminal getOrCreateTerminal(String id) {
        return getOrCreateNode(id, terminalMap, terminals, (nodeId, termId) -> new Terminal(id, nodeId, termId));
    }

    public int getNodeCount() {
        return nodes.size();
    }

    Production createProduction(Nonterminal nonterminal, List<AliasedNode> aliasedNodes, String codeBlock) {
        Production production = new Production(nonterminal, productions.size(), aliasedNodes, codeBlock);
        productions.add(production);
        if (start == null) {
            start = nonterminal;
        }
        return production;
    }

    public void print(Appendable out) throws IOException {
        out.append("start: ").append(start.toString()).append('\n');
        for (Production production : this) {
            production.print(out);
        }
    }
}
