package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;
/**
 * Terminator.
 */

public final class Terminator implements Bytes {
    private final Bytes origin;
    /**
     * Creates a new {@code Terminator}.
     */

    public Terminator() {
        this.origin = new Utf8(
            new Text.Of("\r\n")
        );
    }
    /**
     * Returns the underlying content.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
