package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.BiFunction;

public class Grammar implements Iterable<Production> {
    private final Map<String, Terminal> terminals = new HashMap<>();
    private final Map<String, Nonterminal> nonterminals = new HashMap<>();
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
    private int nodeId;

    public void addHeaderCodeBlock(String block) {
        headerCodeBlock = headerCodeBlock == null ? block : headerCodeBlock + block;
    }


    public Collection<Nonterminal> getNonterminals() {
        return nonterminals.values();
    }

    public Collection<Terminal> getTerminals() {
        return terminals.values();
    }

    public void setStart(String start) {
        this.start = getOrCreateNonterminal(start);
    }

    private <N extends Node> N getOrCreateNode(String id, Map<String, N> map, BiFunction<Integer, Integer, N> factory) {
        N node;
        if (!map.containsKey(id)) {
            map.put(id, node = factory.apply(nodeId++, map.size()));
        } else {
            node = map.get(id);
        }
        return node;
    }

    public boolean isEmpty() {
        return productions.isEmpty();
    }

    public int size() {
        return productions.size();
    }

    @Override
    public Iterator<Production> iterator() {
        return productions.iterator();
    }

    public Nonterminal getOrCreateNonterminal(String id) {
        return getOrCreateNode(id, nonterminals, (nodeId, nonTermId) -> new Nonterminal(this, id, nodeId, nonTermId));
    }

    public Terminal getOrCreateTerminal(String id) {
        return getOrCreateNode(id, terminals, (nodeId, termId) -> new Terminal(id, nodeId, termId));
    }

    public int getNodeCount() {
        return nodeId;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "nonterminals=" + nonterminals +
                ", start='" + start + '\'' +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", headerCodeBlock='" + headerCodeBlock + '\'' +
                '}';
    }

    Production createProduction(Nonterminal nonterminal, List<Production.Member> members, String codeBlock) {
        Production production = new Production(nonterminal, productions.size(), members, codeBlock);
        productions.add(production);
        return production;
    }
}
