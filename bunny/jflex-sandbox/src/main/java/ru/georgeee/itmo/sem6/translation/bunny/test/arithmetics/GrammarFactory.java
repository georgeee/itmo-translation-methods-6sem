package ru.georgeee.itmo.sem6.translation.bunny.test.arithmetics;

import ru.georgeee.itmo.sem6.translation.bunny.grammar.CanonicalGrammar;

public class GrammarFactory {
    private static final String ENCLOSED = "Enclosed";
    private static final String NUM = "Enclosed";

    //Enclosed -> Num | ( Open )
//Level0 -> UOp0 Level1 Lev0R
//Level0R -> BOp0 Level0 Level0R | ε
//Level1 -> UOp1 Enclosed Level1R
//Level1R -> BOp1 Level1 Level1R | ε
//BOp0 -> + | -
//BOp1 -> * | /
//UOp0 -> - | ε
//UOp1 -> ε

    private static CanonicalGrammar createGrammar(){
        CanonicalGrammar grammar = new CanonicalGrammar()
    }
}
