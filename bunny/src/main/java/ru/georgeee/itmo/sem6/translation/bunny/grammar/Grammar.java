package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Grammar {
    @Getter @Setter
    private String start;
    private final Map<String, Nonterminal> nonterminals = new HashMap<>();

    public void addRule(String nonterminalId, List<Node> production) {
        getOrCreate(nonterminalId).addRule(production);
    }

    public Nonterminal getOrCreate(String nonterminalId) {
        Nonterminal nonterminal;
        if (nonterminals.containsKey(nonterminalId)) {
            nonterminals.put(nonterminalId, nonterminal = new Nonterminal(nonterminalId));
        } else {
            nonterminal = nonterminals.get(nonterminalId);
        }
        return nonterminal;
    }

    public Set<String> nonterminalIds() {
        return nonterminals.keySet();
    }

    public Collection<Nonterminal> nonterminals() {
        return nonterminals.values();
    }
}
