package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

public final class ByteView implements Bytes {
    private final HttpSpec src;

    public ByteView(final HttpSpec s) {
        this.src = s;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.bytes();
    }
}
