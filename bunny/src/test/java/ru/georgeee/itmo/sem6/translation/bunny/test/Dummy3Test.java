package ru.georgeee.itmo.sem6.translation.bunny.test;

import org.junit.Test;
import ru.georgeee.itmo.sem6.translation.bunny.runtime.ParseException;

import java.io.IOException;

import static ru.georgeee.itmo.sem6.translation.bunny.test.Dummy3Token.*;

public class Dummy3Test {
    @Test
    public void test1() throws IOException, ParseException {
        test(NUM);
    }
    @Test
    public void test4() throws IOException, ParseException {
        test(MINUS, NUM);
    }
//    @Test
//    public void test5() throws IOException, ParseException {
//        test();
//    }

    @Test
    public void test3() throws IOException, ParseException {
        test(MINUS, NUM, PLUS, NUM, MINUS, NUM);
    }

    @Test
    public void test2() throws IOException, ParseException {
        test(NUM, PLUS, NUM);
    }

    @Test
    public void test6() throws IOException, ParseException {
        test(NUM, MINUS, OBR, NUM, PLUS, NUM, CBR);
    }

    private void test(Dummy3Token... tokens) throws IOException, ParseException {
        new Dummy3Parser(TestTokenReader.getReader(tokens)).parse();
    }
}
