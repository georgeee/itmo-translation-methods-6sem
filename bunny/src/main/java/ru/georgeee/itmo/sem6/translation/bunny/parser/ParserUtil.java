package ru.georgeee.itmo.sem6.translation.bunny.parser;

import java.io.InputStream;

public class ParserUtil {
    public static GrammarParser createParser(InputStream input) {
        GrammarLexer lexer = new GrammarLexer(input);
        return new GrammarParser(lexer);
    }
}
