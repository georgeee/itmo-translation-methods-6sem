package ru.georgeee.itmo.sem6.translation.bunny.parser;

import lombok.Getter;

import java.util.List;

public class PreProduction {
    @Getter
    private final List<Member> children;
    @Getter
    private final String codeBlock;

    public PreProduction(List<Member> children, String codeBlock) {
        this.children = children;
        this.codeBlock = codeBlock;
    }

    public static class Member {
        @Getter
        private final String id;
        @Getter
        private final String alias;
        @Getter
        private final boolean terminal;

        public Member(String id, String alias, boolean terminal) {
            this.id = id;
            this.alias = alias;
            this.terminal = terminal;
        }
    }

}
