package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

public final class HeadersOf implements Bytes {
    private final HttpResponse src;

    public HeadersOf(final HttpResponse r) {
        this.src = r;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.headers();
    }
}
