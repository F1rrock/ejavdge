package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
/**
 * Contract for part.
 */

@FunctionalInterface
public interface Part {
    /**
     * Returns the underlying content.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */
    byte[] content() throws InvariantViolation;
    /**
     * Of wrapper or view over its constructor arguments.
     */

    final class Of implements Part {
        private final Bytes src;
        /**
         * Creates a new {@code Of}.
         * @param bs the bytes
         */

        public Of(final Bytes bs) {
            this.src = bs;
        }
        /**
         * Returns the underlying content.
         * @return the byte
         * @throws InvariantViolation if an invariant is violated
         */

        @Override
        public byte[] content() throws InvariantViolation {
            return this.src.content();
        }
    }
}
