package ru.georgeee.itmo.sem6.translation.lab02;

import lombok.Getter;

/**
 * Created by georgeee on 11.05.15.
 */
public class Either<A, B> {
    private Either() {
    }

    public A getLeft() {
        throw new UnsupportedOperationException("Not a left");
    }

    public B getRight() {
        throw new UnsupportedOperationException("Not a right");
    }

    public final static class Left<A, B> extends Either<A, B> {
        @Getter
        private final A left;

        public Left(A left) {
            this.left = left;
        }
    }

    public final static class Right<A, B> extends Either<A, B> {
        @Getter
        private final B right;

        public Right(B right) {
            this.right = right;
        }
    }


}
