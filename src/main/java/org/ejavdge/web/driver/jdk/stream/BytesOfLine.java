package org.ejavdge.web.driver.jdk.stream;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.util.stream.IntStream;

import java.io.ByteArrayOutputStream;

public final class BytesOfLine implements Bytes {
    private final int[] src;

    public BytesOfLine(final int[] src) {
        this.src = src;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return IntStream.of(this.src)
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::write,
                (l, r) -> {}
            )
            .toByteArray();
    }
}
