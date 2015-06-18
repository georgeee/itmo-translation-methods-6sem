package ru.georgeee.itmo.sem6.translation.bunny.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

public abstract class Parser<E extends Enum<E>, T extends Token<E>> {
    private static final Logger log = LoggerFactory.getLogger(Parser.class);
    private final TableHolder tables;
    private final Map<E, Integer> tokenMapping;
    private final TokenReader<E, T> reader;
    private final GrammarResolver grammar;

    protected Parser(TableHolder tables, TokenReader<E, T> reader, GrammarResolver grammar, Class<E> clazz) {
        this.tables = tables;
        this.reader = reader;
        this.grammar = grammar;
        this.tokenMapping = new EnumMap<>(clazz);
    }

    protected void doParse() throws ParseException, IOException {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        T token = reader.nextToken();
        log.debug("Init: read {}", token);
        while (true){
            int currentState = stack.getFirst();
            log.debug("===== Iteration: {}, token={}", stack, token);
            int actionIndex = getActionIndex(token);
            Action action = tables.actionType[currentState][actionIndex];
            int id = tables.action[currentState][actionIndex];
            if (action == null) {
                throw new ParseException("Syntax error");
            }
            log.debug("current: {} {}", action, id);
            switch (action) {
                case REDUCE:
                    log.info("Production matched : {}", id);
                    outputCallback(id);
                    int leftId = grammar.getLeftNonTermId(id);
                    for (int i = 0; i < grammar.getProductionSize(id); ++i) {
                        int popped = stack.pop();
                        log.debug("popped {}", id, popped);
                    }
                    log.debug("New state: {}", stack.getFirst());
                    int gotoState = tables.goTo[stack.getFirst()][leftId];
                    stack.push(gotoState);
                    log.debug("pushed {}", id);
                    break;
                case SHIFT:
                    if (token == null) {
                        throw new ParseException("Syntax error: unexpected end of input");
                    }
                    tokenReadCallback(token);
                    token = reader.nextToken();
                    stack.push(id);
                    log.debug("read {}", token);
                    log.debug("pushed {}", id);
                    break;
                case ACCEPT:
                    log.info("accepted");
                    return;
            }
        }
    }

    private int getActionIndex(T token) throws ParseException {
        return token == null ? 0 : resolveTerminalId(token) + 1;
    }

    protected void outputCallback(int id) {
    }

    protected void tokenReadCallback(T token) {

    }

    private int resolveTerminalId(T token) throws ParseException {
        Integer id = tokenMapping.get(token.getType());
        if (id == null) {
            id = grammar.getTerminalIndex(token.getTypeId());
            if (id == -1) {
                throw new ParseException("Unexpected token: id=" + token.getTypeId() + " token=" + token);
            }
            tokenMapping.put(token.getType(), id);
        }
        return id;
    }

}
