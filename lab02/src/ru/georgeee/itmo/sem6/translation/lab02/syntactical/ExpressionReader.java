package ru.georgeee.itmo.sem6.translation.lab02.syntactical;

import ru.georgeee.itmo.sem6.translation.lab02.exception.ParseException;
import ru.georgeee.itmo.sem6.translation.lab02.exception.TokenParseException;
import ru.georgeee.itmo.sem6.translation.lab02.expr.BinaryOp;
import ru.georgeee.itmo.sem6.translation.lab02.expr.Expression;
import ru.georgeee.itmo.sem6.translation.lab02.expr.UnaryOp;
import ru.georgeee.itmo.sem6.translation.lab02.expr.Value;
import ru.georgeee.itmo.sem6.translation.lab02.parse_tree.ParseNode;
import ru.georgeee.itmo.sem6.translation.lab02.parse_tree.ParseTreeLogger;
import ru.georgeee.itmo.sem6.translation.lab02.reader.AbstractSequentialReader;
import ru.georgeee.itmo.sem6.translation.lab02.reader.SequentialReader;
import ru.georgeee.itmo.sem6.translation.lab02.token.ArithmeticSign;
import ru.georgeee.itmo.sem6.translation.lab02.token.Num;
import ru.georgeee.itmo.sem6.translation.lab02.token.SignType;
import ru.georgeee.itmo.sem6.translation.lab02.token.Token;

/**
 * Syntactic parser for grammar:
 *
 * Enclosed -> Num | ( Open )
 * Level0 -> UOp0 Level1 Lev0R
 * Level0R -> BOp0 Level0 Level0R | ε
 * Level1 -> UOp1 Enclosed Level1R
 * Level1R -> BOp1 Level1 Level1R | ε
 *
 * BOp0 -> + | -
 * BOp1 -> * | /
 * UOp0 -> - | ε
 * UOp1 -> ε
 *
 */
public class ExpressionReader extends AbstractSequentialReader<Expression, ParseException> {
    private static final Level[] levels = new Level[]{
            new Level(new Object[]{
                    SignType.MINUS, UnaryOp.Type.NEG
            }, new Object[]{
                    SignType.MINUS, BinaryOp.Type.SUBTRACT,
                    SignType.PLUS, BinaryOp.Type.ADD
            }),
            new Level(new Object[0], new Object[]{
                    SignType.MUL, BinaryOp.Type.MUL,
                    SignType.DIV, BinaryOp.Type.DIV
            })
    };
    private final SequentialReader<Token, TokenParseException> tokenReader;
    private final ParseTreeLogger treeLogger;

    public ExpressionReader(SequentialReader<Token, TokenParseException> tokenReader) {
        this.tokenReader = tokenReader;
        treeLogger = new ParseTreeLogger();
    }

    @Override
    protected Expression readImpl() throws ParseException {
        treeLogger.reset();
        Token token = tokenReader.lookAhead();
        if (token == null) {
            return null;
        }
        return readLevel(0);
    }

    private void assertLevel(int level) {
        if (level >= levels.length) {
            throw new IllegalArgumentException("There is no level " + level);
        }
    }

    private UnaryOp.Type readUnaryType(int level) throws TokenParseException {
        assertLevel(level);
        treeLogger.openNode("UnaryOp " + level);
        Token token = tokenReader.lookAhead();
        UnaryOp.Type type = null;
        if (token instanceof ArithmeticSign) {
            SignType sign = ((ArithmeticSign) token).getType();
            type = levels[level].getUnaryTypes().get(sign);
        }
        if (type == null) {
            treeLogger.epsilon();
        } else {
            tokenReader.read();
            treeLogger.string(type.getRepresentation());
        }
        treeLogger.closeNode();
        return type;
    }

    private BinaryOp.Type tryReadBinaryOpType(int level) throws TokenParseException {
        assertLevel(level);
        Token token = tokenReader.lookAhead();
        BinaryOp.Type type = null;
        if (token instanceof ArithmeticSign) {
            SignType sign = ((ArithmeticSign) token).getType();
            type = levels[level].getBinaryTypes().get(sign);
        }
        if (type != null) {
            tokenReader.read();
            treeLogger.openNode("BinaryOp " + level);
            treeLogger.string(type.getRepresentation());
            treeLogger.closeNode();
        }
        return type;
    }

    private Expression readLevel(int level) throws ParseException {
        assertLevel(level);
        treeLogger.openNode("Level " + level);
        UnaryOp.Type unaryType = readUnaryType(level);
        Expression leftOperand;
        if (level == levels.length - 1) {
            leftOperand = readEnclosed();
        } else {
            leftOperand = readLevel(level + 1);
        }
        if (unaryType != null) {
            leftOperand = new UnaryOp(leftOperand, unaryType);
        }
        Expression result = readLevelR(level, leftOperand);
        treeLogger.closeNode();
        return result;
    }

    private Expression readLevelR(int level, Expression leftOperand) throws ParseException {
        assertLevel(level);
        treeLogger.openNode("LevelR " + level);
        BinaryOp.Type binaryOpType = tryReadBinaryOpType(level);
        Expression result;
        if (binaryOpType == null) {
            treeLogger.epsilon();
            result = leftOperand;
        } else {
            Expression rightOperand = readLevel(level);
            result = readLevelR(level, new BinaryOp(leftOperand, rightOperand, binaryOpType));
        }
        treeLogger.closeNode();
        return result;
    }

    private Expression readEnclosed() throws ParseException {
        treeLogger.openNode("Enclosed");
        Expression result;
        Token token = tokenReader.read();
        if (token instanceof Num) {
            treeLogger.string(String.valueOf(((Num) token).getValue()));
            result = new Value(((Num) token).getValue());
        } else if (token instanceof ArithmeticSign) {
            if (!isSign(token, SignType.OPEN_BRACKET)) {
                throw new ParseException("Unexpected sign token : " + token);
            }
            result = readLevel(0);
            token = tokenReader.read();
            if (!isSign(token, SignType.CLOSE_BRACKET)) {
                throw new ParseException("Unexpected token : " + token);
            }
        } else {
            throw new AssertionError();
        }
        treeLogger.closeNode();
        return result;
    }

    private boolean isSign(Token token, SignType signType) {
        if (token instanceof ArithmeticSign) {
            if (((ArithmeticSign) token).getType() == signType) {
                return true;
            }
        }
        return false;
    }


    public ParseNode getLastParsingLog() {
        return treeLogger.getRoot();
    }

    @Override
    public void close() throws TokenParseException {
        tokenReader.close();
    }
}
