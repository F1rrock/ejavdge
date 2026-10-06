package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.*;

/**
 * A {@link Part} that represents a plain text field inside a
 * {@code multipart/form-data} body.
 * <p>
 * A text part consists of a {@code Content-Disposition} header naming the
 * form field, a blank line separating headers from the body, and the raw
 * text value encoded as UTF-8 bytes. The name is required to be
 * non-empty; passing an empty name results in an
 * {@link InvariantViolation} being thrown when the part is materialized.
 * <p>
 * The part can be constructed either from an explicit name and value, or
 * from precomputed byte content when the full part bytes have already been
 * assembled elsewhere.
 */
public final class TextPart implements Part {

    /**
     * The underlying byte content of the part.
     */
    private final Bytes src;

    /**
     * Creates a text part with the given field name and value.
     * <p>
     * The name is embedded into the {@code Content-Disposition} header.
     * It is required to be non-empty, and an {@link InvariantViolation}
     * is thrown otherwise when the part is materialized. The value is
     * encoded as UTF-8 bytes.
     *
     * @param n the name of the form field, must be non-empty
     * @param v the value of the form field
     */
    public TextPart(final Text n, final Text v) {
        this(
            new Utf8(
                new Concat(
                    new Stencil(
                        new Text.Of(
                            """
                            Content-Disposition: form-data; name="%s"\r
                            \r
                            """
                        ),
                        new TextAbout(
                            "text variable name",
                            new NonEmpty(n)
                        )
                    ),
                    v
                )
            )
        );
    }

    /**
     * Creates a text part from precomputed byte content.
     * <p>
     * This constructor is intended for cases where the full part bytes
     * have already been assembled, for example when reconstructing a
     * multipart payload from an existing request.
     *
     * @param s the raw bytes of the part
     */
    public TextPart(final Bytes s) {
        this.src = s;
    }

    /**
     * Returns the raw bytes of this part, including its headers and body.
     *
     * @return the part content as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the part, for example if the field name is
     *         empty
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
