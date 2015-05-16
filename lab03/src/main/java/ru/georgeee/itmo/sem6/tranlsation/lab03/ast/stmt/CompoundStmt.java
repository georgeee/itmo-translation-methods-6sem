package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt;

import lombok.Getter;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CompoundStmt implements Stmt {
    @Getter
    private final List<Stmt> statements;

    public CompoundStmt(Stmt... statements) {
        this(new ArrayList<>(Arrays.asList(statements)));
    }

    public CompoundStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException {
        try {
            for (Stmt stmt : statements) {
                ctx = stmt.validate(ctx);
            }
        } catch (NullPointerException e) {
            throw new ValidationException(e);
        }
        return ctx;
    }

    public void add(Stmt stmt) {
        statements.add(stmt);
    }
}
