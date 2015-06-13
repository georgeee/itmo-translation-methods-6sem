package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Nonterminal implements Node{
    private static final Logger log = LoggerFactory.getLogger(Nonterminal.class);

    @Getter
    private final String id;
    @Getter
    private final List<List<Node>> rules;

    public Nonterminal(String id) {
        this.id = id;
        this.rules = new ArrayList<>();
    }

    public boolean addRule(List<Node> nodes) {
        return rules.add(nodes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Nonterminal nodes = (Nonterminal) o;

        if (!id.equals(nodes.id)) return false;

        log.error("Two nonterminal objects with same id {}", id);
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
