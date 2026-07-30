package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicReference;

public final class Memo implements Bytes {
    private final Bytes origin;
    private final AtomicReference<byte[]> cache;

    public Memo(final Bytes origin) {
        this.origin = origin;
        this.cache = new AtomicReference<>(new byte[0]);
    }

    @Override
    public byte[] content() throws InvariantViolation {
        byte[] result = this.cache.get();
        if (result.length == 0) {
            result = this.origin.content();
            this.cache.set(result);
        }
        return result.clone();
    }
}
