package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Nonterminal implements Node, Iterable<Production> {
    private static final Logger log = LoggerFactory.getLogger(Nonterminal.class);

    private final Grammar grammar;
    @Getter
    private final String id;
    @Getter
    private final int nodeId;
    @Getter
    private final int nonterminalId;
    private final List<Production> productions;
    @Getter
    private List<Attr> attributes;

    Nonterminal(Grammar grammar, String id, int nodeId, int nonterminalId) {
        this.grammar = grammar;
        this.id = id;
        this.nodeId = nodeId;
        this.nonterminalId = nonterminalId;
        this.productions = new ArrayList<>();
    }

    @Override
    public Iterator<Production> iterator() {
        return productions.iterator();
    }

    public int size() {
        return productions.size();
    }

    public boolean isEmpty() {
        return productions.isEmpty();
    }

    public void addProduction(List<Production.Member> members, String codeBlock) {
        productions.add(grammar.createProduction(this, members, codeBlock));
    }

    public void setAttributes(List<Attr> attributes) {
        if (this.attributes == null) {
            this.attributes = attributes;
        } else {
            log.warn("Attributes are already set for nonterminal {}", id);
        }
    }

    @Override
    public boolean equals(Object o) {
        //Invariant
        return this == o;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return '$' + getId();
    }
}
