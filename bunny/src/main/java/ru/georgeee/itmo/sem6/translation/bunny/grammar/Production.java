package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

import java.util.List;

public class Production {
    @Getter
    private final Nonterminal parent;
    @Getter
    private final List<Member> nodes;
    @Getter
    private final String codeBlock;

    Production(Nonterminal parent, List<Member> nodes, String codeBlock) {
        this.parent = parent;
        this.nodes = nodes;
        this.codeBlock = codeBlock;
    }

    @Override
    public String toString() {
        return "Production{" +
                "nodes=" + nodes +
                ", codeBlock='" + codeBlock + '\'' +
                '}';
    }

    public static class Member {
        @Getter
        private final Node node;
        @Getter
        private final String alias;

        public Member(Node node, String alias) {
            this.node = node;
            this.alias = alias;
        }
    }
}
