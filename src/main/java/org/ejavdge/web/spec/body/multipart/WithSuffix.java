package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;
/**
 * With suffix.
 */

public final class WithSuffix implements Bytes {
    private final Bytes origin;
    /**
     * Creates a new {@code WithSuffix}.
     * @param bs the bs
     */

    public WithSuffix(final byte[] bs) {
        this(new Bytes.Of(bs));
    }
    /**
     * Creates a new {@code WithSuffix}.
     * @param bs the bs
     */

    public WithSuffix(final Bytes bs) {
        this(
            new Utf8(new Text.Of("--")),
            bs
        );
    }
    /**
     * Creates a new {@code WithSuffix}.
     * @param suf the suf
     * @param bs the bs
     */

    public WithSuffix(final Bytes suf, final Bytes bs) {
        this.origin = new Concat(bs, suf);
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
