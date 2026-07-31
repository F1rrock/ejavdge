package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class Memo implements Bytes {
    private final Bytes origin;
    private final AtomicReference<byte[]> cache;
    private final AtomicBoolean evaluated;

    public Memo(final Bytes origin) {
        this.origin = origin;
        this.cache = new AtomicReference<>(new byte[0]);
        this.evaluated = new AtomicBoolean(false);
    }

    @Override
    public byte[] content() throws InvariantViolation {
        byte[] result = this.cache.get();
        if (!evaluated.get()) {
            result = this.origin.content();
            this.cache.set(result);
            this.evaluated.set(true);
        }
        return result.clone();
    }
}
