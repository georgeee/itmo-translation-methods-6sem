package ru.georgeee.itmo.sem6.translation.lab03;

import ru.georgeee.itmo.sem6.translation.bunny.runtime.ParseException;
import ru.georgeee.itmo.sem6.translation.lab03.ast.stmt.CompoundStmt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        } catch (ParseException | ValidationException e) {
            System.err.println("Compilation error: " + e);
        } catch (GenerationException e) {
            System.err.println("Generation error: " + e);
        }
    }


    public void compile(PrefixParser parser, Appendable output, Generator generator) throws GenerationException, ValidationException, IOException, ParseException {
        CompoundStmt program = parser.parse().v;
        program.validate(new HashMap<>());
        generator.generate(program, output);
    }

    private InputStream getInputStream() throws IOException {
        if (args.length == 0 || args[0].equals("-")) {
            return System.in;
        } else {
            return new FileInputStream(args[0]);
        }
    }

    public Generator getGenerator() {
        return new JavaGenerator();
    }

    public PrefixParser getParser(InputStream input) {
        PrefixLexer lexer = new PrefixLexer(input);
        return new PrefixParser(lexer);
    }
}
