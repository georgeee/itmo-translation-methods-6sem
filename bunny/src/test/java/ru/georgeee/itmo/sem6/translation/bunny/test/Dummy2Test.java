package ru.georgeee.itmo.sem6.translation.bunny.test;

import org.junit.Test;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.ParseException;

import java.io.IOException;

import static ru.georgeee.itmo.sem6.translation.bunny.test.Dummy2Token.*;

public class Dummy2Test {

    @Test
    public void testOne() throws IOException, ParseException {
        test(D, C, C, C, D);
    }

    private void test(Dummy2Token... tokens) throws IOException, ParseException {
        new Dummy2Parser(TestTokenReader.getReader(tokens)).parse();
    }
}
