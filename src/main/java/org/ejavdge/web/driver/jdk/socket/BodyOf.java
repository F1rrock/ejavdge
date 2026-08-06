package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;

import java.io.ByteArrayOutputStream;

public final class BodyOf implements Bytes {
    private final HttpResponse src;
    private final Num size;

    public BodyOf(final HttpResponse src, final Num size) {
        this.src = src;
        this.size = size;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.body()
            .limit(this.size.value())
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::write,
                (l, r) -> {}
            )
            .toByteArray();
    }
}
