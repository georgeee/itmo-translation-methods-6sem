package ru.georgeee.itmo.sem6.translation.bunny.runtime;

import java.util.HashMap;
import java.util.Map;

public class RuntimeGrammarResolver implements GrammarResolver {
    private final Map<String, Integer> terminalIdMapping = new HashMap<>();
    private final int[] productionSizes;
    private final int[] leftIds;

    public RuntimeGrammarResolver(String[] terminalIds, int[] productionSizes, int[] leftIds) {
        for (int i = 0; i < terminalIds.length; ++i) {
            terminalIdMapping.put(terminalIds[i], i);
        }
        this.productionSizes = productionSizes;
        this.leftIds = leftIds;
    }

    @Override
    public int getTerminalIndex(String name) {
        return terminalIdMapping.get(name);
    }

    @Override
    public int getProductionSize(int productionId) {
        return productionSizes[productionId];
    }

    @Override
    public int getLeftNonTermId(int productionId) {
        return leftIds[productionId];
    }
}
