package ru.georgeee.itmo.sem6.translation.bunny.arithmetics;

import ru.georgeee.itmo.sem6.translation.bunny.arithmetics.expr.Expr;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.ParseException;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.TokenReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStream is = getInputStream(args);
        TokenReader<ASym, ASymbol<?>> lexer = new ALexer(is);
        AParser parser = new AParser(lexer);
        Expr expr;
        do {
            try {
                AParser.CtxLevel0 ctxLevel0 = parser.parse();
                if (ctxLevel0 == null) {
                    break;
                }
                expr = ctxLevel0.v;
                System.out.println(expr);
            } catch (ParseException e) {
                System.err.println("Parse error: " + e.getMessage());
            }
        }
        while (true);
    }

    private static InputStream getInputStream(String[] args) throws FileNotFoundException {
        if (args.length > 0) {
            return new FileInputStream(args[0]);
        }
        return System.in;
    }

}
