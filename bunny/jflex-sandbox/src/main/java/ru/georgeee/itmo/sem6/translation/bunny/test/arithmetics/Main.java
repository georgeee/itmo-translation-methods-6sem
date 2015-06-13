package ru.georgeee.itmo.sem6.translation.bunny.test.arithmetics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream is = getInputStream(args);
        ALexer lexer = new ALexer(is);
        ASymbol symbol = null;
        do {
            if (symbol != null) {
                System.out.println(symbol);
            }
            symbol = lexer.next_token();
        } while (!(symbol == null || symbol.getType() == ASym.EOF));
    }

    private static InputStream getInputStream(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            return new FileInputStream(args[0]);
        }
        return System.in;
    }

}
