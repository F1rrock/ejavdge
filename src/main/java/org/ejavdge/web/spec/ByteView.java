package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * An adapter that exposes an {@link HttpSpec} as a {@link Bytes} value.
 * <p>
 * An HTTP spec already produces its content as a byte array via
 * {@link HttpSpec#bytes()}, but it is not itself a {@code Bytes} instance.
 * This class bridges the two abstractions, allowing an HTTP message to be
 * used wherever a {@code Bytes} value is expected — for example, so that a
 * request can be memoized, concatenated with other byte sequences,
 * measured, or validated through the same combinators used for other byte
 * content.
 * <p>
 * The {@link #content()} method simply delegates to
 * {@link HttpSpec#bytes()}, so no additional processing is performed and
 * the message's bytes are returned unchanged.
 */
public final class ByteView implements Bytes {

    /**
     * The HTTP spec whose bytes are exposed.
     */
    private final HttpSpec src;

    /**
     * Creates a byte view over the given HTTP spec.
     *
     * @param s the HTTP spec whose bytes will be exposed as a
     *          {@code Bytes} value
     */
    public ByteView(final HttpSpec s) {
        this.src = s;
    }

    /**
     * Returns the raw bytes of the underlying HTTP spec.
     *
     * @return the HTTP message as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the message
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.bytes();
    }
}
