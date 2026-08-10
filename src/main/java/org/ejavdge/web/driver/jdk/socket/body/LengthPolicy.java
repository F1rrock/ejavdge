package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.resource.ContentLength;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class LengthPolicy implements UnaryOperator<IntStream> {
    private final Num len;
    private final UnaryOperator<IntStream> next;

    public LengthPolicy(final byte[] bs, final UnaryOperator<IntStream> nxt) {
        this.len = new ContentLength(
            new Bytes.Of(bs)
        );
        this.next = nxt;
    }

    @Override
    public IntStream apply(final IntStream s) {
        int size;
        try {
            size = this.len.value();
        } catch (final InvariantViolation e) {
            return this.next.apply(s);
        }
        return new WithLimitation(
            new Num.Of(size)
        ).apply(s);
    }
}
