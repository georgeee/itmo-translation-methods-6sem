package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Grammar {
    private final Map<String, Nonterminal> nonterminals = new HashMap<>();
    @Getter @Setter
    private String start;
    @Getter @Setter
    private String packageName;
    @Getter @Setter
    private String className;
    @Getter @Setter
    private String headerCodeBlock;

    public void addHeaderCodeBlock(String block){
        headerCodeBlock = headerCodeBlock == null ? block : headerCodeBlock + block;
    }

    public Nonterminal getOrCreate(String nonterminalId) {
        Nonterminal nonterminal;
        if (!nonterminals.containsKey(nonterminalId)) {
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
}
