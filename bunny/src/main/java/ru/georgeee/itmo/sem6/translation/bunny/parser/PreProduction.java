package ru.georgeee.itmo.sem6.translation.bunny.parser;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PreProduction {
    @Getter
    private final List<Pair<String, String>> children;
    @Getter
    private final String codeBlock;

    public PreProduction(List<Pair<String, String>> children, String codeBlock) {
        this.children = children;
        this.codeBlock = codeBlock;
    }
}
