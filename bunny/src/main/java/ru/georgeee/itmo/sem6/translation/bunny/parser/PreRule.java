package ru.georgeee.itmo.sem6.translation.bunny.parser;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PreRule {
    private final String leftId;
    private final List<Pair<String, String>> returns;
    private final List<PreProduction> productions;

    public PreRule(String leftId, List<Pair<String, String>> returns, List<PreProduction> productions) {
        this.leftId = leftId;
        this.returns = returns;
        this.productions = productions;
    }
}
