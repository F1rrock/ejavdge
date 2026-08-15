package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

public final class Positive implements Num {
    private final Num origin;

    public Positive(final Num origin) {
        this.origin = origin;
    }

    @Override
    public int value() throws InvariantViolation {
        final var v = this.origin.value();
        if (v > 0) {
            return v;
        }
        throw new InvariantViolation("Value is not positive.");
    }
}
