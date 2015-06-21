package ru.georgeee.itmo.sem6.translation.bunny;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Grammar;
import ru.georgeee.itmo.sem6.translation.bunny.parser.GrammarLexer;
import ru.georgeee.itmo.sem6.translation.bunny.parser.GrammarParser;
import ru.georgeee.itmo.sem6.translation.bunny.processing.FirstFollow;
import ru.georgeee.itmo.sem6.translation.bunny.processing.Processor;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.Generator;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.TableHolder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private boolean print;
    private List<Path> inputPaths = new ArrayList<>();
    private Path outputDir;
    private static final String OPTION_PRINT = "-p";
    private static final String BUNNY_EXTENSION = ".bn";

    public Main(String[] args) throws FileNotFoundException {
        List<String> files = new ArrayList<>();
        for (String arg : args) {
            switch (arg) {
                case OPTION_PRINT:
                    print = true;
                    break;
                default:
                    files.add(arg);
            }
        }
        if (files.size() < 2) {
            System.err.println("Syntax: java -jar bunny.jar [-p] input_file1, input_file2, ... output_dir");
            throw new IllegalArgumentException("Wrong input format");
        }
        for (int i = 0; i < files.size() - 1; ++i) {
            inputPaths.add(getPath(files.get(i)));
        }
        outputDir = getPath(files.get(files.size() - 1));
        if (!Files.isDirectory(outputDir)) {
            throw new IllegalArgumentException("Output dir " + outputDir + " is not a directory");
        }
    }

    private Path getPath(String fileName) throws FileNotFoundException {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            throw new FileNotFoundException(fileName);
        }
        return path;
    }

    public static void main(String[] args) throws IOException {
        new Main(args).process();
    }

    private void process() throws IOException {
        try {
            for (Path path : inputPaths) {
                if (Files.isDirectory(path)) {
                    Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            String fileName = file.getFileName().toString();
                            if (fileName.endsWith(BUNNY_EXTENSION)) {
                                run(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } else {
                    run(path);
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e);
        } catch (RecognitionException e) {
            System.err.println("Compilation error: " + e);
        }
    }

    public void run(Path file) throws IOException {
        run(getParser(getInputStream(file)));
    }

    public void run(GrammarParser parser) throws IOException {
        Grammar grammar = parser.grammarDef().v;
        if (print) {
            printGrammar(grammar);
        }
        Processor processor = new Processor(grammar);
        processor.compute();
        if (print) {
            printProcessor(processor);
        }
        TableHolder tables = processor.createTableHolder();
        Generator generator = new Generator(grammar, tables);
        generator.generate(getOutPath(grammar));
    }

    private void printGrammar(Grammar grammar) throws IOException {
        FirstFollow firstFollow = grammar.createFirstFollow();
        firstFollow.compute();
        System.out.println("Grammar:");
        grammar.print(System.out);
        System.out.println("---------- Sets -----------");
        firstFollow.print(System.out);
        System.out.println("---------- Item Sets -----------");
    }

    private void printProcessor(Processor processor) throws IOException {
        processor.printItemSets(System.out);
        System.out.println("---------- Transitions -----------");
        processor.printTransitions(System.out);
        System.out.append("=============== Extended grammar ================\n");
        processor.printExtendedGrammar(System.out);
        try (BufferedWriter csvOut = Files.newBufferedWriter(Paths.get("action-goto.csv"))) {
            processor.printActionGotoTablesCsv(csvOut);
        }
    }


    private Path getOutPath(Grammar grammar) throws IOException {
        Path path = outputDir.resolve(grammar.getPackageName().replace('.', '/'));
        Files.createDirectories(path);
        return path;
    }

    private CharStream getInputStream(Path inputPath) throws IOException {
        return new ANTLRFileStream(inputPath.toString());
    }

    public GrammarParser getParser(CharStream input) {
        GrammarLexer lexer = new GrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new GrammarParser(tokens);
    }
}

