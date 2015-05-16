package ru.georgeee.itmo.sem6.translation.lab02;

import org.apache.commons.lang3.StringUtils;
import ru.georgeee.itmo.sem6.translation.lab02.exception.ParseException;
import ru.georgeee.itmo.sem6.translation.lab02.expr.Expression;
import ru.georgeee.itmo.sem6.translation.lab02.parse_tree.ParseNode;
import ru.georgeee.itmo.sem6.translation.lab02.syntactical.ExpressionReader;
import ru.georgeee.itmo.sem6.translation.lab02.token.TokenReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;


public class Main {
    private static final String TEST_FILE_SUFFIX = ".test";
    private static final String UTF_8 = StandardCharsets.UTF_8.toString();
    private final Config config;

    public Main(Config config) {
        this.config = config;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            new Main(new Config(args[0], args[1])).run();
        } else {
            throw new IllegalArgumentException("Accepts two args: input, output directories");
        }
    }

    private void run() throws IOException {
        Files.walkFileTree(config.inDir, Collections.<FileVisitOption>emptySet(), 1, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (attrs.isRegularFile() && file.getFileName().toString().endsWith(TEST_FILE_SUFFIX)) {
                    runTest(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void runTest(Path testFile) throws IOException {
        String testName = StringUtils.removeEnd(testFile.getFileName().toString(), TEST_FILE_SUFFIX);
        int i = 1;
        try (TokenReader tokenReader = new TokenReader(new FileReader(testFile.toFile()));
             ExpressionReader expressionReader = new ExpressionReader(tokenReader)) {
            Expression expression;
            while ((expression = expressionReader.read()) != null) {
                ParseNode log = expressionReader.getLastParsingLog();
                writeToFile(getOutFile(testName, i, ".out"), expression.toString(), " = ", String.valueOf(expression.evaluate()));
                writeToFile(getOutFile(testName, i, ".dot"), ParseNode.toDot(log));
                ++i;
            }
        } catch (ParseException e) {
            try (PrintWriter pw = new PrintWriter(getOutFile(testName, i, ".err").toFile(), UTF_8)) {
                pw.print("Error occured during parsing: ");
                e.printStackTrace(pw);
            } catch (UnsupportedEncodingException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    private void writeToFile(Path file, String... lines) throws IOException {
        Files.write(file, Arrays.asList(lines), StandardCharsets.UTF_8);
    }

    private Path getOutFile(String testName, int i, String suffix) {
        return config.outDir.resolve(testName + (i == 1 ? "" : "_" + i) + suffix);
    }

    private static final class Config {
        final Path inDir;
        final Path outDir;

        private Config(String inDir, String outDir) {
            this(Paths.get(inDir), Paths.get(outDir));
        }

        private Config(Path inDir, Path outDir) {
            assertIsDirectory(inDir);
            assertIsDirectory(outDir);
            this.inDir = inDir;
            this.outDir = outDir;
        }

        private void assertIsDirectory(Path dir) {
            if (!Files.isDirectory(dir)) {
                throw new IllegalStateException("Not a directory : " + dir);
            }
        }
    }
}
