package org.ejavdge.web.driver.jdk.stream;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.util.stream.IntStream;

import java.io.ByteArrayOutputStream;

public final class BytesOfLine implements Bytes {
    private final IntStream src;

    public BytesOfLine(final int ...src) {
        this.src = IntStream.of(src);
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::write,
                (l, r) -> {}
            )
            .toByteArray();
    }
}
