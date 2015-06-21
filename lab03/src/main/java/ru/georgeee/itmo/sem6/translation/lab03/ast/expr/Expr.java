package ru.georgeee.itmo.sem6.translation.lab03.ast.expr;

import ru.georgeee.itmo.sem6.translation.lab03.ast.AstNode;
import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;

import java.util.Map;

public interface Expr extends AstNode {
    Map<String, Var> getUsedVars();
}
