package ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt;

import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.AstNode;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ValidationException;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;

import java.util.Map;

public interface Stmt extends AstNode {
    Map<String, Var> validate(Map<String, Var> ctx) throws ValidationException;
}
