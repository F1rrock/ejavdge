package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
/**
 * Byte view.
 */

public final class ByteView implements Bytes {
    private final HttpSpec src;
    /**
     * Creates a new {@code ByteView}.
     * @param s the 's' argument
     */

    public ByteView(final HttpSpec s) {
        this.src = s;
    }
    /**
     * Returns the underlying content.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.bytes();
    }
}
