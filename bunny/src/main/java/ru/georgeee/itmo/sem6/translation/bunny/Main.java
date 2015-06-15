package ru.georgeee.itmo.sem6.translation.bunny;

import org.antlr.v4.runtime.*;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.parser.GrammarLexer;
import ru.georgeee.itmo.sem6.translation.bunny.parser.GrammarParser;
import ru.georgeee.itmo.sem6.translation.bunny.processing.Processor;
import ru.georgeee.itmo.sem6.translation.bunny.processing.SetsComputer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        SetsComputer setsComputer = grammar.createSetsComputer();
        setsComputer.compute();
        System.out.println("Grammar:");
        grammar.print(System.out);
        System.out.println("---------- Sets -----------");
        setsComputer.print(System.out);
        Processor processor = new Processor(grammar);
        processor.compute();
        System.out.println("---------- Item Sets -----------");
        processor.printItemSets(System.out);
        System.out.println("---------- Transitions -----------");
        processor.printTransitions(System.out);
        System.out.append("=============== Extended grammar ================\n");
        processor.printExtendedGrammar(System.out);
        try (BufferedWriter csvOut = Files.newBufferedWriter(Paths.get("action-goto.csv"))) {
            processor.printActionGotoTables(csvOut);
        }
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

