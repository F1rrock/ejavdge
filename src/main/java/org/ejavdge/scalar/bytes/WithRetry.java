package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;

public final class WithRetry implements Bytes {
    private final Bytes origin;
    private final Num attempts;

    public WithRetry(final Bytes origin, final Num attempts) {
        this.origin = origin;
        this.attempts = new Positive(attempts);
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.content(this.attempts.value());
    }

    private byte[] content(final int left) throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation err) {
            if (left == 1) {
                throw new InvariantViolation("Retry failed.", err);
            }
            try {
                return this.content(left - 1);
            } catch (final InvariantViolation failure) {
                err.addSuppressed(failure);
                throw new InvariantViolation("Retry failed.", err);
            }
        }
    }
}