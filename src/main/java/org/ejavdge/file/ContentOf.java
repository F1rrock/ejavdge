package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

public final class ContentOf implements Bytes {
    private final ByteFile src;

    public ContentOf(final ByteFile f) {
        this.src = f;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
