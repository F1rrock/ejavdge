package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * A contract for a single part inside a {@code multipart/form-data} body.
 * <p>
 * Each part contributes a fragment of the overall multipart payload — for
 * example a text field, a file attachment, or any other named entry. A
 * part is responsible only for producing its own bytes, including any
 * per-part headers such as {@code Content-Disposition} or
 * {@code Content-Type}. The enclosing boundaries and the top-level
 * {@code Content-Type} header of the request are handled by
 * {@link Multipart}, not by the part itself.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #content()}. Implementations can be created directly from a
 * {@link Bytes} value via the nested {@link Of} class, or by dedicated
 * types such as {@link FilePart} and {@link TextPart}.
 */
@FunctionalInterface
public interface Part {

    /**
     * Returns the raw bytes of this part.
     * <p>
     * The returned array contains the entire part, including its own
     * headers (if any) and its body, but not the surrounding boundary
     * markers — those are added by {@link Multipart}.
     *
     * @return the part as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the part
     */
    byte[] content() throws InvariantViolation;

    /**
     * A simple {@link Part} implementation that wraps a {@link Bytes}
     * value.
     * <p>
     * This is useful when the bytes of the part have already been
     * assembled elsewhere — for example by concatenating headers and a
     * body — and only need to be adapted to the {@code Part} interface.
     * The {@link #content()} method simply delegates to the underlying
     * {@link Bytes#content()}.
     */
    final class Of implements Part {

        /**
         * The underlying byte content of the part.
         */
        private final Bytes src;

        /**
         * Creates a part from the given byte content.
         *
         * @param bs the raw bytes of the part
         */
        public Of(final Bytes bs) {
            this.src = bs;
        }

        /**
         * Returns the raw bytes of the underlying {@link Bytes} value.
         *
         * @return the part content as a byte array
         * @throws InvariantViolation if an invariant is violated while
         *         materializing the content
         */
        @Override
        public byte[] content() throws InvariantViolation {
            return this.src.content();
        }
    }
}
