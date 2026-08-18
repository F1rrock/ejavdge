package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.num.Num;

public final class HasStatus implements Bytes {
    private final Bytes origin;
    private final Num expected;

    public HasStatus(final Num n, final Bytes bs) {
        this.origin = bs;
        this.expected = n;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final var bs = new Memo(this.origin);
        final int e = this.expected.value();
        final int a = new Status(bs).value();
        if (e == a) {
            return bs.content();
        }
        throw new InvariantViolation(
            "Expected status " + e + " but got " + a
        );
    }
}
