package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;

public final class Request implements HttpSpec {
    private final Bytes src;

    public Request(final HttpSpec origin) {
        this(
            new Concat(
                new ByteView(origin),
                new Terminator()
            )
        );
    }

    public Request(final Bytes bs) {
        this.src = bs;
    }

    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
