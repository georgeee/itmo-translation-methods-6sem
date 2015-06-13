package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

import java.util.*;

public class CanonicalGrammar {
    @Getter
    private final Nonterminal start;
    private final Map<String, Nonterminal> nonterminals;

    public CanonicalGrammar(Nonterminal start) {
        this.start = start;
        nonterminals = new HashMap<>();
        nonterminals.put(start.getId(), start);
    }

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
