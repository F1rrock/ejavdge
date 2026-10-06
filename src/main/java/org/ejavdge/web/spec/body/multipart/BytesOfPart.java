package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * An adapter that exposes a {@link Part} as a {@link Bytes}.
 * <p>
 * A multipart part already produces its content as a byte array, but is
 * not itself a {@code Bytes} instance. This class bridges the two
 * abstractions, allowing a part to be used wherever a {@code Bytes} value
 * is expected — for example, when a part needs to be memoized, concatenated
 * with other byte sequences, or measured.
 * <p>
 * The {@link #content()} method simply delegates to
 * {@link Part#content()}, so no additional processing is performed and the
 * part's bytes are returned unchanged.
 */
public final class BytesOfPart implements Bytes {

    /**
     * The part whose content is exposed as bytes.
     */
    private final Part src;

    /**
     * Creates a byte view over the given multipart part.
     *
     * @param p the part whose content will be exposed as bytes
     */
    public BytesOfPart(final Part p) {
        this.src = p;
    }

    /**
     * Returns the raw content of the underlying part.
     *
     * @return the part's content as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the part's content
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
