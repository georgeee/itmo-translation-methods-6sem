package ru.georgeee.itmo.sem6.translation.bunny.runtime;

public interface GrammarResolver {
    int getTerminalIndex(String name);
    int getProductionSize(int productionId);
    int getLeftNonTermId(int productionId);
}
