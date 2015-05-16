package ru.georgeee.itmo.sem6.translation.lab02.parse_tree;

import java.util.ArrayDeque;
import java.util.Deque;

public class ParseTreeLogger {
    private final Deque<ParseNode> stack;

    public ParseTreeLogger() {
        stack = new ArrayDeque<>();
        reset();
    }

    public void openNode(String name) {
        stack.push(new ParseNode(name));
    }

    public void closeNode() {
        ParseNode closingNode = stack.pop();
        if (stack.isEmpty()) {
            stack.push(closingNode);
            throw new IllegalStateException("No node is opened");
        }
        stack.getFirst().add(closingNode);
    }

    public ParseNode currentNode() {
        return stack.getFirst();
    }

    public void string(String terminal) {
        currentNode().add(terminal);
    }

    public ParseNode getRoot() {
        return stack.getLast();
    }

    public void reset() {
        stack.clear();
        stack.add(new ParseNode("ROOT"));
    }

    public void epsilon() {
        currentNode().add("ε");
    }
}
