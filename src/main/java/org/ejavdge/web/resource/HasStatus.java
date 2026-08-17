package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.num.Num;

public final class HasStatus implements Bytes {
    private final Bytes origin;
    private final Num expected;
    private final Num actual;

    public HasStatus(final Num n, final Bytes bs) {
        this.origin = new Memo(bs);
        this.expected = n;
        this.actual = new Status(this.origin);
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final int e = this.expected.value();
        final int a = this.actual.value();
        if (e == a) {
            return this.origin.content();
        }
        throw new InvariantViolation(
            "Expected status " + e + " but got " + a
        );
    }
}
