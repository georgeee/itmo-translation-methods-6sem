package ru.georgeee.itmo.sem6.translation.bunny.grammar;

import lombok.Getter;

import java.util.Iterator;
import java.util.List;

public class Production {
    @Getter
    private final Nonterminal parent;
    private final List<Member> nodes;
    @Getter
    private final String codeBlock;

    Production(Nonterminal parent, List<Member> nodes, String codeBlock) {
        this.parent = parent;
        this.nodes = nodes;
        this.codeBlock = codeBlock;
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public Iterator<Member> iterator() {
        return nodes.iterator();
    }

    public Member get(int index) {
        return nodes.get(index);
    }

    @Override
    public String toString() {
        return "Production{" +
                "parent=" + parent.getId() +
                ", nodes=" + nodes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production that = (Production) o;

        if (!nodes.equals(that.nodes)) return false;
        if (!parent.equals(that.parent)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parent.hashCode();
        result = 31 * result + nodes.hashCode();
        return result;
    }

    public static class Member {
        @Getter
        private final Node node;
        @Getter
        private final String alias;

        @Override
        public String toString() {
            return alias + ':' + node.getId();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Member member = (Member) o;

            if (!node.equals(member.node)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return node.hashCode();
        }

        public Member(Node node, String alias) {
            this.node = node;
            this.alias = alias;
        }
    }
}
