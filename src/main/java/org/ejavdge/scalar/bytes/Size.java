package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

public final class Size implements Num {
    private final Bytes src;

    public Size(final Bytes bs) {
        this.src = bs;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.src.content().length;
    }
}
