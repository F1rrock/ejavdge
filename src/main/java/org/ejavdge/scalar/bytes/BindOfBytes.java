package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Function;

public final class BindOfBytes implements Bytes {
    private final Bytes origin;
    private final Function<byte[], Bytes> binding;

    public BindOfBytes(final Bytes bs, final Function<byte[], Bytes> f) {
        this.origin = bs;
        this.binding = f;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.binding.apply(
            this.origin.content()
        ).content();
    }
}
