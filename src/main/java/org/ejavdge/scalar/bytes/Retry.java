package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Supplier;

public final class Retry implements Bytes {

    private final Supplier<? extends Bytes> factory;
    private final int attempts;

    public Retry(
        final Supplier<? extends Bytes> factory,
        final int attempts
    ) {
        if (attempts < 1) {
            throw new IllegalArgumentException(
                "attempts must be greater than 0"
            );
        }

        this.factory = factory;
        this.attempts = attempts;
    }

    public Retry(
        final Bytes origin,
        final int attempts
    ) {
        this(() -> origin, attempts);
    }

    @Override
    public byte[] content() {
        InvariantViolation last = new InvariantViolation("retry failed");

        for (int attempt = 0; attempt < this.attempts; ++attempt) {
            try {
                return this.factory.get().content();
            } catch (final InvariantViolation err) {
                last = err;
            }
        }

        throw last;
    }
}