package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.io.ByteArrayOutputStream;

public final class HeadersOf implements Bytes {
    private final HttpResponse src;

    public HeadersOf(final HttpResponse src) {
        this.src = src;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.headers()
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::write,
                (l, r) -> {}
            )
            .toByteArray();
    }
}
