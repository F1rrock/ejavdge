package org.ejavdge.web.spec.header;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.spec.Terminator;

public final class Header implements Bytes {
    private final Bytes src;

    public Header(final Text n, final Num v) {
        this(n, new TextOfNum(v));
    }

    public Header(final Text n, final Text v) {
        this(
            new Concat(
                new Utf8(
                    new Stencil(
                        new Text.Of("%s: %s"),
                        n, v
                    )
                ),
                new Terminator()
            )
        );
    }

    public Header(final Bytes bs) {
        this.src = bs;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
