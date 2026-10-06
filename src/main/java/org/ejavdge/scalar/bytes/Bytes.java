package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

/**
 * A contract for an immutable sequence of bytes.
 * <p>
 * This is the central abstraction for binary data throughout the application.
 * Implementations provide access to raw bytes via {@link #content()}, which
 * returns a copy of the underlying data so that callers cannot mutate the
 * internal state of the instance.
 * <p>
 * This is a functional interface whose functional method is {@link #content()}.
 * It is used wherever binary data needs to be represented uniformly, such as
 * for HTTP payloads, session cookies, file contents, and intermediate results
 * of byte-level transformations.
 */
@FunctionalInterface
public interface Bytes {

    /**
     * Returns the underlying content as a byte array.
     * <p>
     * Implementations should return a copy of the internal data to preserve
     * immutability. The returned array may be modified by the caller without
     * affecting the state of this instance.
     *
     * @return the byte content as an array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    byte[] content() throws InvariantViolation;

    /**
     * A simple {@link Bytes} implementation that wraps a fixed byte array.
     * <p>
     * The internal array is defensively copied on construction and on each call
     * to {@link #content()}, so the instance is fully immutable and safe to
     * share across threads and callers.
     */
    final class Of implements Bytes {

        /**
         * The wrapped byte array.
         */
        private final byte[] bs;

        /**
         * Creates a byte sequence from the given array.
         * <p>
         * The array is cloned on construction, so subsequent modifications to
         * the caller's array do not affect this instance.
         *
         * @param bs the byte array to wrap
         */
        public Of(final byte[] bs) {
            this.bs = bs.clone();
        }

        /**
         * Returns a copy of the wrapped byte array.
         *
         * @return a clone of the byte content
         */
        @Override
        public byte[] content() {
            return this.bs.clone();
        }
    }
}
