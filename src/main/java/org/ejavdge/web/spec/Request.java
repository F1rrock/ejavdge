package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
/**
 * Request.
 */

public final class Request implements HttpSpec {
    private final Bytes src;
    /**
     * Creates a new {@code Request}.
     * @param origin the origin
     */

    public Request(final HttpSpec origin) {
        this(
            new Concat(
                new ByteView(origin),
                new Terminator()
            )
        );
    }
    /**
     * Creates a new {@code Request}.
     * @param bs the bs
     */

    public Request(final Bytes bs) {
        this.src = bs;
    }
    /**
     * Returns the HTTP message as bytes.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
