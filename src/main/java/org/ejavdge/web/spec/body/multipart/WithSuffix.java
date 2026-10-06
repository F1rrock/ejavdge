package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Bytes} decorator that appends a suffix to the content of
 * another byte sequence.
 * <p>
 * This class is the counterpart of {@link WithPrefix} and is used when
 * assembling a {@code multipart/form-data} body: after the final part,
 * the body must be closed with the standard closing delimiter
 * {@code "--<boundary>--"}. Rather than hard-coding the trailing
 * {@code "--"} at each call site, {@code WithSuffix} wraps a boundary
 * value and produces the boundary's own bytes followed by {@code "--"}.
 * <p>
 * The suffix can be supplied explicitly as raw bytes or as a {@link Bytes}
 * value; when only the boundary is given, the standard {@code "--"} suffix
 * is added automatically. The class is immutable and computes the
 * concatenated bytes only when {@link #content()} is called.
 */
public final class WithSuffix implements Bytes {

    /**
     * The underlying concatenated byte content (body + suffix).
     */
    private final Bytes origin;

    /**
     * Creates a suffixed byte sequence from the given raw bytes, using the
     * standard {@code "--"} suffix.
     *
     * @param bs the raw bytes to which the suffix is appended
     */
    public WithSuffix(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    /**
     * Creates a suffixed byte sequence from the given bytes, using the
     * standard {@code "--"} suffix.
     *
     * @param bs the bytes to which the suffix is appended
     */
    public WithSuffix(final Bytes bs) {
        this(
            new Utf8(new Text.Of("--")),
            bs
        );
    }

    /**
     * Creates a suffixed byte sequence from the given suffix and content.
     * <p>
     * The resulting content is the concatenation {@code bs + suf},
     * materialized lazily by {@link #content()}. This constructor is
     * useful when the suffix is not the standard {@code "--"}, for
     * example when a custom terminator is required.
     *
     * @param suf the suffix bytes
     * @param bs  the content bytes to which the suffix is appended
     */
    public WithSuffix(final Bytes suf, final Bytes bs) {
        this.origin = new Concat(bs, suf);
    }

    /**
     * Returns the concatenated bytes (content followed by suffix).
     *
     * @return the suffixed content as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the underlying byte sequences
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
