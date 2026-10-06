package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;
/**
 * With prefix.
 */

public final class WithPrefix implements Bytes {
    private final Bytes origin;
    /**
     * Creates a new {@code WithPrefix}.
     * @param bs the bs
     */

    public WithPrefix(final byte[] bs) {
        this(new Bytes.Of(bs));
    }
    /**
     * Creates a new {@code WithPrefix}.
     * @param bs the bs
     */

    public WithPrefix(final Bytes bs) {
        this(
            new Utf8(new Text.Of("--")),
            bs
        );
    }
    /**
     * Creates a new {@code WithPrefix}.
     * @param pref the pref
     * @param bs the bs
     */

    public WithPrefix(final Bytes pref, final Bytes bs) {
        this.origin = new Concat(pref, bs);
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
