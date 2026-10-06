package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
/**
 * HTTP message fragment represented as bytes.
 */

@FunctionalInterface
public interface HttpSpec {
    /**
     * Returns the HTTP message as bytes.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */
    byte[] bytes() throws InvariantViolation;
    /**
     * Of wrapper or view over its constructor arguments.
     */

    final class Of implements HttpSpec {
        final Bytes src;
        /**
         * Creates a new {@code Of}.
         * @param src the src
         */

        public Of(final byte[] src) {
            this(new Bytes.Of(src));
        }
        /**
         * Creates a new {@code Of}.
         * @param src the bytes
         */

        public Of(final Bytes src) {
            this.src = src;
        }
        /**
         * Returns the HTTP message as bytes.
         * @return the byte
         */

        @Override
        public byte[] bytes() {
            return this.src.content();
        }
    }
}
