package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;
import lombok.Setter;
import ru.georgeee.itmo.sem6.translation.bunny.processing.FirstFollow;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.GrammarResolver;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class Grammar implements Iterable<Production>, GrammarResolver {
    private final Map<String, Terminal> terminalMap = new HashMap<>();
    private final Map<String, Nonterminal> nonterminalMap = new HashMap<>();
    private final List<Production> productions = new ArrayList<>();
    @Getter
    private Nonterminal start;
    @Getter
    private Nonterminal pseudoStart;
    @Getter
    @Setter
    private String tokenType;
    @Getter
    @Setter
    private String enumType;
    @Getter
    @Setter
    private String packageName;
    @Getter
    @Setter
    private String className;
    @Getter
    @Setter
    private String headerCodeBlock;
    @Getter
    private final List<Nonterminal> nonterminals = new ArrayList<>();
    @Getter
    private final List<Terminal> terminals = new ArrayList<>();
    @Getter
    private final List<Node> nodes = new ArrayList<>();

    public Grammar() {
        this.start = getOrCreateNonterminal("<start>");
    }

    public void addHeaderCodeBlock(String block) {
        headerCodeBlock = headerCodeBlock == null ? block : headerCodeBlock + block;
    }

    public void bindStart(String id) {
        start.addProduction(Arrays.asList(new AliasedNode(pseudoStart = getOrCreateNonterminal(id), null)), "");
    }

    private <N extends Node> N getOrCreateNode(String id, Map<String, N> map, List<N> list, BiFunction<Integer, Integer, N> factory) {
        N node;
        if (!map.containsKey(id)) {
            node = factory.apply(nodes.size(), list.size());
            nodes.add(node);
            list.add(node);
            map.put(id, node);
        } else {
            node = map.get(id);
        }
        return node;
    }

    public Production get(int index) {
        return productions.get(index);
    }

    public boolean isEmpty() {
        return productions.isEmpty();
    }

    public int size() {
        return productions.size();
    }

    public FirstFollow createFirstFollow() {
        return new FirstFollow(productions, nodes, start);
    }

    @Override
    public Iterator<Production> iterator() {
        return productions.iterator();
    }

    public Nonterminal getOrCreateNonterminal(String id) {
        return getOrCreateNode(id, nonterminalMap, nonterminals, (nodeId, nonTermId) -> new Nonterminal(this, id, nodeId, nonTermId));
    }

    public Terminal getOrCreateTerminal(String id) {
        return getOrCreateNode(id, terminalMap, terminals, (nodeId, termId) -> new Terminal(id, nodeId, termId));
    }

    public int getNodeCount() {
        return nodes.size();
    }

    Production createProduction(Nonterminal nonterminal, List<AliasedNode> aliasedNodes, String codeBlock) {
        Production production = new Production(nonterminal, productions.size(), aliasedNodes, codeBlock);
        productions.add(production);
        return production;
    }

    public void print(Appendable out) throws IOException {
        out.append("start: ").append(start.toString()).append('\n');
        for (Production production : this) {
            production.print(out);
        }
    }

    @Override
    public int getTerminalIndex(String name) {
        Terminal terminal = terminalMap.get(name);
        if (terminal == null) return -1;
        return terminal.getTermId();
    }

    @Override
    public int getProductionSize(int productionId) {
        return productions.get(productionId).size();
    }

    @Override
    public int getLeftNonTermId(int productionId) {
        return productions.get(productionId).getParent().getNontermId();
    }

    public boolean isStart(Node node) {
        return node.unwrap() == start.unwrap();
    }
}
