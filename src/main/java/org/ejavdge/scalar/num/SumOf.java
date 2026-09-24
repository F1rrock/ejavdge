package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

public final class SumOf implements Num {
    private final Num left;
    private final Num right;

    public SumOf(final Num left, final Num right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.left.value() + this.right.value();
    }
}
