package ru.georgeee.itmo.sem6.translation.bunny.runtime;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.*;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Generator {
    private final Grammar grammar;
    private final TableHolder tableHolder;

    public Generator(Grammar grammar, TableHolder tableHolder) {
        this.grammar = grammar;
        this.tableHolder = tableHolder;
    }

    public void generate(PrintStream out) {
        out.append("package ").append(grammar.getPackageName()).println(';');
        out.println(str(grammar.getHeaderCodeBlock()));
        out.println("import ru.georgeee.itmo.sem6.translation.bunny.runtime.*;");
        out.println("import static ru.georgeee.itmo.sem6.translation.bunny.runtime.Action.*;");
        out.println("import java.util.*;");
        out.println("import java.io.IOException;");
        out.format("public class %s extends Parser<%s, %s> {%n", grammar.getClassName(), grammar.getEnumType(), grammar.getTokenType());
        generateVars(out);
        generateConstructor(out);
        generateMethods(out);
        generateContexts(out);
        out.println('}');
    }

    private void generateMethods(PrintStream out) {
        generateOutputCallback(out);
        generateTokenRead(out);
        generateParseMethod(out);
    }

    private void generateParseMethod(PrintStream out) {
        Nonterminal pseusoStart = grammar.getPseudoStart();
        String className = getCtxClassName(pseusoStart.getId());
        out.printf("public %s parse() throws ParseException, IOException{%n", className);
        out.println("if(!doParse()) return null;");
        out.printf("return (%s) stack.pop();%n", className);
        out.println("}");
    }

    private void generateOutputCallback(PrintStream out) {
        out.println("protected void outputCallback(int productionId){");
        out.println("switch(productionId){");
        for (Production production : grammar) {
            if (production.getParent().equals(grammar.getStart())) {
                continue;
            }
            out.printf("case %d:{%n", production.getId());
            String className = getCtxClassName(production.getParent().getId());
            for (int i = 0; i < production.size(); ++i) {
                out.printf("Object v%d = stack.pop();%n", production.size() - 1 - i);
            }
            out.printf("%s ctx = new %s();%n ctx.production%d(", className, className, production.getId());
            for (int i = 0; i < production.size(); ++i) {
                if (i > 0) {
                    out.printf(", ");
                }
                out.printf("(%s) v%d", getTypeName(production.get(i)), i);
            }
            out.printf(");%n stack.push(ctx);%n}%nbreak;%n");
        }
        out.println("}");
        out.println("}");
    }
    private void generateTokenRead(PrintStream out){
        out.printf("protected void tokenReadCallback(%s token){%n", grammar.getTokenType());
        out.println("stack.push(token);");
        out.println("}");
    }

    private String replaceVariable(String code, String search, String replacement) {
        return Pattern.compile(Pattern.quote(search) + "([^_A-Za-z0-9])").matcher(code).replaceAll(replacement + "$1");
    }

    private void generateContexts(PrintStream out) {
        for (Nonterminal nonterminal : grammar.getNonterminals()) {
            if (nonterminal.equals(grammar.getStart())) {
                continue;
            }
            out.format("public static class %s{%n", getCtxClassName(nonterminal.getId()));
            List<Attr> attrs = getAttrs(nonterminal);
            for (Attr attr : attrs) {
                out.format("public %s %s;%n", attr.getType(), attr.getName());
            }
            for (Production production : nonterminal) {
                out.format("public void production%d(", production.getId());
                String code = production.getCodeBlock();
                String genCode = null;
                for (int i = 0; i < production.size(); ++i) {
                    AliasedNode node = production.get(i);
                    String name = node.getAlias();
                    String search = name;
                    if (name == null) {
                        name = node.getId() + i;
                        search = node.getId();
                    }
                    if (node.isTerminal()) {
                        name = "term" + StringUtils.capitalize(name);
                    }
                    if (code != null) {
                        search = (node.isTerminal() ? "@" : "%") + search;
                        code = replaceVariable(code, search, name);
                    } else if (genCode == null && node.isNonterminal()) {
                        genCode = "$v = " + name + ".v;";
                    }
                    if (i > 0) {
                        out.print(", ");
                    }
                    out.printf("%s %s", getTypeName(node), name);
                }
                if (code == null) {
                    code = genCode;
                }
                out.println("){");
                if (code != null) {
                    for (Attr attr : attrs) {
                        String name = attr.getName();
                        code = replaceVariable(code, "$" + name, "this." + name);
                    }
                    out.println(code);
                }
                out.println("}");
            }
            out.println('}');
        }
    }

    private List<Attr> getAttrs(Nonterminal nonterminal) {
        List<Attr> attrs = nonterminal.getAttributes();
        if (attrs == null || attrs.isEmpty()) {
            attrs = Arrays.asList(new Attr("Object", "v"));
        }
        return attrs;
    }

    private String getTypeName(Node node) {
        if (node.isNonterminal()) {
            return getCtxClassName(node.getId());
        } else {
            return grammar.getTokenType();
        }
    }

    private String getCtxClassName(String nontermId) {
        return "Ctx" + nontermId;
    }

    private void generateConstructor(PrintStream out) {
        //        public Parser(TableHolder tables, TokenReader<E, T> reader, GrammarResolver grammar, Class<E> clazz) {
        out.format("public %s (TokenReader<%s, %s> reader){%n", grammar.getClassName(), grammar.getEnumType(), grammar.getTokenType());
        out.print("super(");
        generateTableHolder(out);
        out.format(",%n reader,%n ");
        generateGrammar(out);
        out.format(", %n %s.class);", grammar.getEnumType());
        out.println('}');
    }

    private void generateGrammar(PrintStream out) {
        //        public RuntimeGrammarResolver(String[] terminalIds, int[] productionSizes, int[] leftIds) {
        out.print("new RuntimeGrammarResolver(");
        generateStringArray(getTerminalIds(), out);
        out.println(",");
        generateIntArray(getProductionSizes(), out, false);
        out.println(",");
        generateIntArray(getLeftIds(), out, false);
        out.print(")");
    }

    private int[] getLeftIds() {
        int[] ids = new int[grammar.size()];
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = grammar.get(i).getParent().getNontermId();
        }
        return ids;
    }

    private int[] getProductionSizes() {
        int[] sizes = new int[grammar.size()];
        for (int i = 0; i < sizes.length; ++i) {
            sizes[i] = grammar.get(i).size();
        }
        return sizes;
    }

    private String[] getTerminalIds() {
        String[] res = new String[grammar.getTerminals().size()];
        for (int i = 0; i < res.length; ++i) {
            res[i] = grammar.getTerminals().get(i).getId();
        }
        return res;
    }

    private void generateTableHolder(PrintStream out) {
        out.print("new TableHolder(");
        generateIntArray2(tableHolder.action, out);
        out.println(",");
        generateActionArray2(tableHolder.actionType, out);
        out.println(",");
        generateIntArray2(tableHolder.goTo, out);
        out.print(")");
    }

    private void generateStringArray(String[] array, PrintStream out) {
        out.print("new String[]{");
        for (int j = 0; j < array.length; ++j) {
            if (j > 0) {
                out.print(",");
            }
            out.append('"').append(StringEscapeUtils.escapeJava(array[j])).append('"');
        }
        out.print("}");
    }

    private void generateIntArray(int[] array, PrintStream out, boolean omit) {
        out.print(omit ? "{" : "new int[]{");
        for (int j = 0; j < array.length; ++j) {
            if (j > 0) {
                out.print(",");
            }
            out.print(array[j]);
        }
        out.print("}");
    }

    private void generateActionArray2(Action[][] array, PrintStream out) {
        out.println("new Action[][]{");
        for (int i = 0; i < array.length; ++i) {
            out.print("{");
            for (int j = 0; j < array[i].length; ++j) {
                if (j > 0) {
                    out.print(",");
                }
                out.print(array[i][j] == null ? "null" : array[i][j].name());
            }
            out.println("},");
        }
        out.print("}");
    }

    private void generateIntArray2(int[][] array, PrintStream out) {
        out.println("new int[][]{");
        for (int i = 0; i < array.length; ++i) {
            generateIntArray(array[i], out, true);
            out.println(",");
        }
        out.print("}");
    }

    private void generateVars(PrintStream out) {
        out.println("private final Deque<Object> stack = new ArrayDeque<>(); ");
    }

    private String str(String s) {
        if (s == null) return "";
        return s;
    }
}
