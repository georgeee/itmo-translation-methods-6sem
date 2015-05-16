package ru.georgeee.itmo.sem6.translation.lab02.parse_tree;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab02.Either;

import java.util.ArrayList;

public class ParseNode extends ArrayList<Either<ParseNode, String>> {
    @Getter
    private final String name;

    public ParseNode(String name) {
        this.name = name;
    }

    public static final StringBuilder toDot(ParseNode node, StringBuilder sb) {
        sb.append("digraph parse_tree{\n");
        node.dumpAsDot(new DotDumpState(), null, sb);
        sb.append("}\n");
        return sb;
    }

    public static final String toDot(ParseNode node) {
        return toDot(node, new StringBuilder()).toString();
    }

    public void add(ParseNode closingNode) {
        add(new Either.Left<ParseNode, String>(closingNode));
    }

    public void add(String terminal) {
        add(new Either.Right<ParseNode, String>(terminal));
    }

    private void dumpAsDot(DotDumpState state, String parentName, StringBuilder sb) {
        String name = state.newName();
        dumpLabel(name, sb);
        if (parentName != null) {
            dumpChild(parentName, name, sb);
        }
        for (Either<ParseNode, String> subNode : this) {
            if (subNode instanceof Either.Left) {
                ParseNode node = subNode.getLeft();
                node.dumpAsDot(state, name, sb);
            } else {
                String terminal = subNode.getRight();
                String terminalName = state.newName();
                dumpChild(name, terminalName, sb);
                dumpLabel(terminalName, sb, terminal);
            }
        }
    }

    private void dumpLabel(String dotName, StringBuilder sb, String label) {
        sb.append(dotName).append(" [label=\"").append(label.replace("\"", "\\\"")).append("\"];\n");
    }

    private void dumpLabel(String dotName, StringBuilder sb) {
        dumpLabel(dotName, sb, getName());
    }

    private void dumpChild(String parentName, String childName, StringBuilder sb) {
        sb.append(parentName).append(" -> ").append(childName).append(";\n");
    }

    private final static class DotDumpState {
        int nameCounter = 0;

        String newName() {
            return "node_" + (++nameCounter);
        }
    }
}
