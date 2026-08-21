package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

public final class BytesOfPart implements Bytes {
    private final Part src;

    public BytesOfPart(final Part p) {
        this.src = p;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
