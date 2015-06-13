package ru.georgeee.itmo.sem6.translation.bunny.arithmetics;

import ru.georgeee.itmo.sem6.translation.bunny.Token;
import ru.georgeee.itmo.sem6.translation.bunny.TokenReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream is = getInputStream(args);
        TokenReader<ASym> lexer = new ALexer(is);
        Token<ASym> symbol = null;
        do {
            if (symbol != null) {
                System.out.println(symbol);
            }
            symbol = lexer.nextToken();
        } while (!(symbol == null || symbol.getType() == ASym.EOF));
    }

    private static InputStream getInputStream(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            return new FileInputStream(args[0]);
        }
        return System.in;
    }

}
