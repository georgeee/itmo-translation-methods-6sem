package ru.georgeee.itmo.sem6.translation.bunny;

import org.antlr.v4.runtime.*;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.GrammarLexer;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.GrammarParser;
import ru.georgeee.itmo.sem6.translation.bunny.processing.Processor;

import java.io.IOException;

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
            run(getParser(getInputStream()));
        } catch (IOException e) {
            System.err.println("I/O error: " + e);
        } catch (RecognitionException e) {
            System.err.println("Compilation error: " + e);
        }
    }

    public void run(GrammarParser parser) throws IOException {
        Grammar grammar = parser.grammarDef().v;
        Processor processor = new Processor(grammar);
        processor.computeItemSets();
        processor.printItemSets(System.out);
    }

    private CharStream getInputStream() throws IOException {
        if (args.length == 0 || args[0].equals("-")) {
            return new ANTLRInputStream(System.in);
        } else {
            return new ANTLRFileStream(args[0]);
        }
    }

    public GrammarParser getParser(CharStream input) {
        GrammarLexer lexer = new GrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new GrammarParser(tokens);
    }
}

