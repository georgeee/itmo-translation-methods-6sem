package ru.georgeee.itmo.sem6.translation.lab03;

import ru.georgeee.itmo.sem6.translation.lab03.ast.AstNode;
import ru.georgeee.itmo.sem6.translation.lab03.ast.Var;
import ru.georgeee.itmo.sem6.translation.lab03.ast.expr.Expr;

import java.util.*;

/**
 * Created by georgeee on 15.05.15.
 */
public class Util {
    public static <A, B> Map<A, B> singletonMap(A a, B b) {
        final Map.Entry<A, B> entry = new AbstractMap.SimpleImmutableEntry<A, B>(a, b);
        return new AbstractMap<A, B>() {
            @Override
            public Set<Entry<A, B>> entrySet() {
                return Collections.singleton(entry);
            }
        };
    }

    public static Map<String, Var> singletonMap(Var var) {
        return Util.singletonMap(var.getName(), var);
    }

    @SafeVarargs
    public static <A, B> Map<A, B> union(Map<? extends A, ? extends B>... maps) {
        Map<A, B> result = new HashMap<>();
        for (Map<? extends A, ? extends B> map : maps) {
            result.putAll(map);
        }
        return result;
    }

    public static void validate(Map<String, Var> ctx, Var var, AstNode holder) throws ValidationException {
        if (ctx.containsKey(var.getName())) {
            validate(var, ctx.get(var.getName()), holder);
        } else {
            throw new ValidationException("no var '" + var + "' declared");
        }
    }

    public static void validate(Var var1, Var var2, AstNode holder) throws ValidationException {
//        if (!var1.equals(var2)) {
//            throw new ValidationException("var '" + var1 + "' is already declared with other type : " + var2);
//        }
    }

    public static void validate(Map<String, Var> ctx, Expr expr) throws ValidationException {
        for (Var var : expr.getUsedVars().values()) {
            validate(ctx, var, expr);
        }
    }
}
