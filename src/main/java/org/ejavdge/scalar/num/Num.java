package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

/**
 * A contract for a numeric value that is materialized on demand.
 * <p>
 * This is the central abstraction for working with integers throughout the
 * application. Implementations of this interface provide a single method,
 * {@link #value()}, which returns an {@code int}. Like {@link
 * org.ejavdge.scalar.text.Text} and {@link org.ejavdge.scalar.bytes.Bytes},
 * this interface is lazy: the underlying computation (which may involve
 * parsing, network access, or other expensive work) is performed only when
 * {@link #value()} is invoked.
 * <p>
 * This is a functional interface whose functional method is {@link #value()}.
 * It is used throughout the application to represent values such as problem
 * identifiers, language indices, retry counts, and page numbers. A number of
 * decorators in this package build on this interface to add behavior such as
 * validation ({@link org.ejavdge.scalar.num.NonNegative},
 * {@link org.ejavdge.scalar.num.Positive}), fallback selection
 * ({@link org.ejavdge.scalar.num.Fallback}), or descriptive labels
 * ({@link org.ejavdge.scalar.num.NumAbout}).
 */
@FunctionalInterface
public interface Num {

    /**
     * Returns the numeric value.
     * <p>
     * Calling this method materializes the value. Implementations may perform
     * expensive operations (such as parsing or network requests) as part of
     * this call.
     *
     * @return the numeric value as an integer
     * @throws InvariantViolation if an invariant is violated while computing
     *         the value
     */
    int value() throws InvariantViolation;

    /**
     * A simple {@link Num} implementation that wraps a fixed integer value.
     * <p>
     * The value is provided at construction time and returned unchanged by
     * {@link #value()}. This is useful for representing literal numeric values
     * without any computation.
     */
    final class Of implements Num {

        /**
         * The wrapped integer value.
         */
        private final int src;

        /**
         * Creates a numeric value from the given integer.
         *
         * @param n the integer value to wrap
         */
        public Of(final int n) {
            this.src = n;
        }

        /**
         * Returns the wrapped integer value.
         *
         * @return the numeric value
         * @throws InvariantViolation if an invariant is violated
         */
        @Override
        public int value() throws InvariantViolation {
            return this.src;
        }
    }
}
