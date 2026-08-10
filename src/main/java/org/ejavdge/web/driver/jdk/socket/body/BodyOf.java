package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.driver.jdk.socket.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class BodyOf implements Bytes {
    private final HttpResponse src;
    private final UnaryOperator<IntStream> policy;

    public BodyOf(final HttpResponse r, final UnaryOperator<IntStream> o) {
        this.src = r;
        this.policy = o;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.policy.apply(this.src.body())
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::write,
                (l, r) -> {}
            )
            .toByteArray();
    }
}
