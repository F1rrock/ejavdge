package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

public final class NonNegative implements Num {
    private final Num origin;

    public NonNegative(final Num n) {
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        final var n = this.origin.value();
        if (n >= 0) {
            return n;
        }
        throw new InvariantViolation("Value is negative");
    }
}
