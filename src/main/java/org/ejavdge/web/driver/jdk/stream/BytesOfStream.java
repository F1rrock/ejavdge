package org.ejavdge.web.driver.jdk.stream;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.io.ByteArrayOutputStream;
import java.util.stream.IntStream;

public final class BytesOfStream implements Bytes {
    private final IntStream src;

    public BytesOfStream(final IntStream s) {
        this.src = s;
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
