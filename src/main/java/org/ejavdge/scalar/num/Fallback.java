package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

public final class Fallback implements Num {
    private final Num origin;
    private final Num then;

    public Fallback(final Num n, final Num f) {
        this.origin = n;
        this.then = f;
    }

    @Override
    public int value() throws InvariantViolation {
        try {
            return this.origin.value();
        } catch (final InvariantViolation e) {
            return this.then.value();
        }
    }
}
