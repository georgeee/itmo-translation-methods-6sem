package ru.georgeee.itmo.sem6.tranlsation.lab03;

import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt.CompoundStmt;

import java.io.IOException;

public interface Generator {
    void generate(CompoundStmt program, Appendable out) throws GenerationException, IOException;
}
