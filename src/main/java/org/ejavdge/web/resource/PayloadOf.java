package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.charset.StandardCharsets;

public final class PayloadOf implements Bytes {
    private final Bytes origin;

    public PayloadOf(final Bytes b) {
        this.origin = b;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final var full = new String(
            this.origin.content(),
            StandardCharsets.ISO_8859_1
        );
        final int idx = full.indexOf("\r\n\r\n");
        return (idx == -1 ? full : full.substring(idx + 4))
            .getBytes(StandardCharsets.ISO_8859_1);
    }
}
