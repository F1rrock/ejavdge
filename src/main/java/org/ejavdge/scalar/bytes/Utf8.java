package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class Utf8 implements Bytes {
    private final Text src;

    public Utf8(final Text t) {
        this.src = t;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src
            .content()
            .getBytes(StandardCharsets.UTF_8);
    }
}
