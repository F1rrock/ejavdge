package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
/**
 * BytesOfPart wrapper or view over its constructor arguments.
 */

public final class BytesOfPart implements Bytes {
    private final Part src;
    /**
     * Creates a new {@code BytesOfPart}.
     * @param p the 'p' argument
     */

    public BytesOfPart(final Part p) {
        this.src = p;
    }
    /**
     * Returns the underlying content.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
