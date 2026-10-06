package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Uuid;

/**
 * A {@link Bytes} that represents the MIME boundary string of a
 * {@code multipart/form-data} payload.
 * <p>
 * Multipart bodies separate their parts with a boundary marker: a short
 * string that is guaranteed not to appear inside any of the parts. This
 * class generates such a marker in the style used by WebKit-based browsers
 * — the prefix {@code "----WebKitFormBoundary"} followed by a random or
 * caller-supplied identifier — and exposes it as raw UTF-8 bytes.
 * <p>
 * By default, the suffix is generated as a fresh {@link Uuid}, so every
 * boundary is unique. A custom identifier or a precomputed byte sequence
 * can be supplied via the other constructors, which is useful when the
 * boundary must match an existing payload or when reproducible output is
 * required.
 */
public final class Boundary implements Bytes {

    /**
     * The underlying byte content of the boundary.
     */
    private final Bytes origin;

    /**
     * Creates a boundary with a freshly generated random identifier.
     * <p>
     * The identifier is a {@link Uuid}, so the resulting boundary is
     * unique for every instance.
     */
    public Boundary() {
        this(new Uuid());
    }

    /**
     * Creates a boundary from the given identifier.
     * <p>
     * The identifier is embedded into the standard WebKit-style boundary
     * template {@code "----WebKitFormBoundary%s"} and encoded as UTF-8
     * bytes.
     *
     * @param id the identifier to append after the boundary prefix
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
     * Creates a boundary from precomputed byte content.
     * <p>
     * This constructor is intended for cases where the boundary string has
     * already been materialized, for example when reconstructing a
     * multipart payload from an existing request.
     *
     * @param bs the raw bytes of the boundary
     */
    public Boundary(final Bytes bs) {
        this.origin = bs;
    }

    /**
     * Returns the raw bytes of this boundary.
     *
     * @return the boundary as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the boundary
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
