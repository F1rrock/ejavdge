package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Bytes} decorator that prepends a prefix to the content of
 * another byte sequence.
 * <p>
 * This class is used when assembling a {@code multipart/form-data} body:
 * each part must be introduced by the standard opening delimiter
 * {@code "--<boundary>"}. Rather than hard-coding that prefix at each call
 * site, {@code WithPrefix} wraps a boundary value and produces the bytes
 * {@code "--"} followed by the boundary's own bytes.
 * <p>
 * The prefix can be supplied explicitly as raw bytes or as a {@link Bytes}
 * value; when only the boundary is given, the standard {@code "--"} prefix
 * is added automatically. The class is immutable and computes the
 * concatenated bytes only when {@link #content()} is called.
 */
public final class WithPrefix implements Bytes {

    /**
     * The underlying concatenated byte content (prefix + body).
     */
    private final Bytes origin;

    /**
     * Creates a prefixed byte sequence from the given raw bytes, using the
     * standard {@code "--"} prefix.
     *
     * @param bs the raw bytes to which the prefix is applied
     */
    public WithPrefix(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    /**
     * Creates a prefixed byte sequence from the given bytes, using the
     * standard {@code "--"} prefix.
     *
     * @param bs the bytes to which the prefix is applied
     */
    public WithPrefix(final Bytes bs) {
        this(
            new Utf8(new Text.Of("--")),
            bs
        );
    }

    /**
     * Creates a prefixed byte sequence from the given prefix and content.
     * <p>
     * The resulting content is the concatenation {@code pref + bs},
     * materialized lazily by {@link #content()}. This constructor is
     * useful when the prefix is not the standard {@code "--"}, for
     * example when the closing boundary of a multipart body is being
     * assembled.
     *
     * @param pref the prefix bytes
     * @param bs   the content bytes to which the prefix is applied
     */
    public WithPrefix(final Bytes pref, final Bytes bs) {
        this.origin = new Concat(pref, bs);
    }

    /**
     * Returns the concatenated bytes (prefix followed by content).
     *
     * @return the prefixed content as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the underlying byte sequences
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
