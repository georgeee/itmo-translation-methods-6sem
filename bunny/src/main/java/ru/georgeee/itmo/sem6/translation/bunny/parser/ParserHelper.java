package ru.georgeee.itmo.sem6.translation.bunny.parser;

import ru.georgeee.itmo.sem6.translation.bunny.grammar.*;

import java.util.ArrayList;
import java.util.List;

public class ParserHelper {
    public static void addRule(Grammar grammar, PreRule preRule) {
        String nonterminalId = preRule.getNonterminalId();
        Nonterminal nonterminal = grammar.getOrCreate(nonterminalId);
        nonterminal.setAttributes(preRule.getAttributes());
        for (PreProduction preProduction : preRule.getProductions()) {
            addProduction(nonterminal, grammar, preProduction);
        }
    }

    private static void addProduction(Nonterminal nonterminal, Grammar grammar, PreProduction preProduction) {
        List<Production.Member> members = new ArrayList<>();
        for (PreProduction.Member preMember : preProduction.getChildren()) {
            Node node;
            if (preMember.isTerminal()) {
                node = new Terminal(preMember.getId());
            } else {
                node = grammar.getOrCreate(preMember.getId());
            }
            members.add(new Production.Member(node, preMember.getAlias()));
        }
        nonterminal.addProduction(members, preProduction.getCodeBlock());
    }

}
