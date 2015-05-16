package ru.georgeee.itmo.sem6.tranlsation.lab03;

import org.antlr.v4.runtime.*;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.tranlsation.lab03.ast.stmt.CompoundStmt;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    private final String[] args;

    public Main(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) throws IOException {
        new Main(args).process();
    }

    private void process() throws IOException {
        try {
            compile(getParser(getInputStream()), System.out, getGenerator());
        } catch (IOException e) {
            System.err.println("I/O error: " + e);
        } catch (ValidationException | RecognitionException e) {
            System.err.println("Compilation error: " + e);
        } catch (GenerationException e) {
            System.err.println("Generation error: " + e);
        }
    }

    public void compile(PrefixParser parser, Appendable output, Generator generator) throws GenerationException, ValidationException, IOException {
        CompoundStmt program = parser.program().v;
        program.validate(new HashMap<String, Var>());
        getGenerator().generate(program, System.out);
    }

    private CharStream getInputStream() throws IOException {
        if (args.length == 0 || args[0].equals("-")) {
            return new ANTLRInputStream(System.in);
        } else {
            return new ANTLRFileStream(args[0]);
        }
    }

    public Generator getGenerator() {
        return new JavaGenerator();
    }

    public PrefixParser getParser(CharStream input) {
        PrefixLexer lexer = new PrefixLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new PrefixParser(tokens);
    }
}
