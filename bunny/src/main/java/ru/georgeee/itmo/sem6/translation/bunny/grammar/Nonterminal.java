package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Nonterminal implements Node, Iterable<Production> {
    private static final int CLASS_HASH_CODE_SALT = Nonterminal.class.hashCode();
    private static final Logger log = LoggerFactory.getLogger(Nonterminal.class);

    @Getter
    private final String id;
    @Getter
    private final List<Production> productions;
    @Getter
    private List<Attr> attributes;

    public Nonterminal(String id) {
        this.id = id;
        this.productions = new ArrayList<>();
    }

    @Override
    public Iterator<Production> iterator() {
        return productions.iterator();
    }

    public boolean addProduction(List<Production.Member> members, String codeBlock) {
        return productions.add(new Production(this, members, codeBlock));
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
        return 31 * id.hashCode() + CLASS_HASH_CODE_SALT;
    }

    @Override
    public String toString() {
        return "Nonterminal{" +
                "id='" + id + '\'' +
                ", productions=" + productions +
                ", attributes=" + attributes +
                '}';
    }
}
