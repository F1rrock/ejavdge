package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.*;

public final class HasStatus implements Bytes {
    private final Bytes origin;
    private final Num expected;
    private final Text message;

    public HasStatus(final Num n, final Bytes bs) {
        this(n, new Empty(), bs);
    }

    public HasStatus(final Num n, final Text t, final Bytes bs) {
        this.origin = bs;
        this.expected = n;
        this.message = new NonEmpty(t);
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
            new Fallback(
                this.message,
                new Concat(
                    new Text.Of(" "),
                    new Items.Of<>(
                        new Text.Of("Expected status"),
                        new TextOfNum(e),
                        new Text.Of("but got"),
                        new TextOfNum(a)
                    )
                )
            ).content()
        );
    }
}
