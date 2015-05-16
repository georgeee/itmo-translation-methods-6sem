package ru.georgeee.itmo.sem6.translation.lab02.syntactical;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.lab02.expr.BinaryOp;
import ru.georgeee.itmo.sem6.translation.lab02.expr.UnaryOp;
import ru.georgeee.itmo.sem6.translation.lab02.token.SignType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by georgeee on 12.05.15.
 */
class Level {
    @Getter
    private final Map<SignType, UnaryOp.Type> unaryTypes;
    @Getter
    private final Map<SignType, BinaryOp.Type> binaryTypes;

    Level(Map<SignType, UnaryOp.Type> unaryTypes, Map<SignType, BinaryOp.Type> binaryTypes) {
        this.unaryTypes = unaryTypes;
        this.binaryTypes = binaryTypes;
    }

    Level(Object[] unaryTypeMap, Object[] binaryTypeMap) {
        this(Level.<SignType, UnaryOp.Type>asMap(unaryTypeMap), Level.<SignType, BinaryOp.Type>asMap(binaryTypeMap));
    }

    private static <A, B> Map<A, B> asMap(Object[] array) {
        Map<A, B> map = new HashMap<>();
        for (int i = 1; i < array.length; i += 2) {
            map.put((A) array[i - 1], (B) array[i]);
        }
        return map;
    }
}
