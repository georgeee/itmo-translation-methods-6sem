package ru.georgeee.itmo.sem6.translation.bunny.parser;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Attr;

import java.util.List;

class PreRule {
    @Getter
    private final String nonterminalId;
    @Getter
    private final List<Attr> attributes;
    @Getter
    private final List<PreProduction> productions;

    public PreRule(String nonterminalId, List<Attr> attributes, List<PreProduction> productions) {
        this.nonterminalId = nonterminalId;
        this.attributes = attributes;
        this.productions = productions;
    }
}
