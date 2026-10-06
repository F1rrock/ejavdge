package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

/**
 * A {@link Bytes} implementation that encodes a {@link Text} as UTF-8 bytes.
 * <p>
 * This class adapts a text value to the {@link Bytes} interface by encoding its
 * content using the UTF-8 charset. When {@link #content()} is called, the
 * underlying text is materialized via {@link Text#content()}, and the resulting
 * string is converted to a byte array using
 * {@link StandardCharsets#UTF_8}.
 * <p>
 * This is useful whenever textual content needs to be transmitted, stored, or
 * processed as raw bytes, such as when constructing request bodies or writing
 * to files.
 */
public final class Utf8 implements Bytes {

    /**
     * The underlying text whose UTF-8 encoding is exposed as bytes.
     */
    private final Text src;

    /**
     * Creates a byte view over the UTF-8 encoding of the given text.
     *
     * @param t the text whose UTF-8 bytes will be exposed
     */
    public Utf8(final Text t) {
        this.src = t;
    }

    /**
     * Returns the UTF-8 encoded bytes of the underlying text.
     * <p>
     * The text content is materialized via {@link Text#content()} and encoded
     * using the UTF-8 charset.
     *
     * @return the UTF-8 byte representation of the underlying text
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the text content
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src
            .content()
            .getBytes(StandardCharsets.UTF_8);
    }
}
