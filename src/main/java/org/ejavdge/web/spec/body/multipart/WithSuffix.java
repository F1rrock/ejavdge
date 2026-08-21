package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;

public final class WithSuffix implements Bytes {
    private final Bytes origin;

    public WithSuffix(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    public WithSuffix(final Bytes bs) {
        this(
            new Utf8(new Text.Of("--")),
            bs
        );
    }

    public WithSuffix(final Bytes suf, final Bytes bs) {
        this.origin = new Concat(bs, suf);
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
