package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Uuid;
/**
 * Boundary.
 */

public final class Boundary implements Bytes {
    private final Bytes origin;
    /**
     * Creates a new {@code Boundary}.
     */

    public Boundary() {
        this(new Uuid());
    }
    /**
     * Creates a new {@code Boundary}.
     * @param id the id
     */

    public Boundary(final Text id) {
        this(
            new Utf8(
                new Stencil(
                    new Text.Of("----WebKitFormBoundary%s"),
                    id
                )
            )
        );
    }
    /**
     * Creates a new {@code Boundary}.
     * @param bs the bs
     */

    public Boundary(final Bytes bs) {
        this.origin = bs;
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
