package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;

public final class WithRetries implements Bytes {
    private final Bytes origin;
    private final Num attempts;

    public WithRetries(final Bytes bs, final int n) {
        this(bs, new Num.Of(n));
    }

    public WithRetries(final Bytes bs, final Num n) {
        this.origin = bs;
        this.attempts = new NumAbout("attempts", new Positive(n));
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final var left = this.attempts.value();
        try {
            return this.origin.content();
        } catch (final InvariantViolation e) {
            if (left <= 1) {
                throw new InvariantViolation(
                    "Bytes can not be obtained within " + left + " retries.\n",
                    e
                );
            }
            return new WithRetries(this.origin, left - 1).content();
        }
    }
}
