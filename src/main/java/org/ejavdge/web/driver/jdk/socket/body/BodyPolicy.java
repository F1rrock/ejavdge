package org.ejavdge.web.driver.jdk.socket.body;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class BodyPolicy implements UnaryOperator<IntStream> {
    private final UnaryOperator<IntStream> origin;

    public BodyPolicy(final byte[] bs) {
        this(
            new LengthPolicy(
                bs,
                new ChunkPolicy(
                    bs,
                    new UnsupportedPolicy()
                )
            )
        );
    }

    public BodyPolicy(final UnaryOperator<IntStream> op) {
        this.origin = op;
    }

    @Override
    public IntStream apply(final IntStream s) {
        return this.origin.apply(s);
    }
}
