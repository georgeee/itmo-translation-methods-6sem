package ru.georgeee.itmo.sem6.translation.bunny.test.arithmetics;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Terminal;

public class ASymbol implements Terminal{
    @Getter
    private final ASym type;
    @Getter
    private final int yyline;
    @Getter
    private final int yycolumn;
    @Getter
    private final Object value;

    public ASymbol(ASym type, int yyline, int yycolumn) {
        this(type, yyline, yycolumn, null);
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "type=" + type +
                ", yyline=" + yyline +
                ", yycolumn=" + yycolumn +
                ", value=" + value +
                '}';
    }

    public ASymbol(ASym type, int yyline, int yycolumn, Object value) {
        this.type = type;
        this.yyline = yyline;
        this.yycolumn = yycolumn;
        this.value = value;
    }

    @Override
    public String getId() {
        return type.getId();
    }
}
