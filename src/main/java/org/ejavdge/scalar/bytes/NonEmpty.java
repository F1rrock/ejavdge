package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

public final class NonEmpty implements Bytes {
    private final Bytes origin;

    public NonEmpty(final Bytes x) {
        this.origin = x;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final byte[] x = this.origin.content();
        if (x.length == 0) {
            throw new InvariantViolation("byte array is empty.");
        }
        return x;
    }
}
