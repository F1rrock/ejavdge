package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A contract for an immutable UTF-16 text value.
 * <p>
 * This is the central abstraction for textual data throughout the application.
 * Implementations provide access to their content as a {@link String} via
 * {@link #content()}, which is materialized on demand. This allows text values
 * to be composed, transformed, and decorated lazily, deferring expensive work
 * (such as network access, parsing, or extraction from an HTML document) until
 * the result is actually needed.
 * <p>
 * This is a functional interface whose functional method is {@link #content()}.
 * It is used wherever text needs to be represented uniformly, such as for
 * messages, extracted page content, formatted stencils, and intermediate
 * results of text transformations.
 */
@FunctionalInterface
public interface Text {

    /**
     * Returns the underlying content as a string.
     * <p>
     * Calling this method materializes the text. Implementations may perform
     * expensive operations (such as network requests or parsing) as part of
     * this call.
     *
     * @return the text content as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    String content() throws InvariantViolation;

    /**
     * A simple {@link Text} implementation that wraps a fixed string value.
     * <p>
     * The string is provided at construction time and returned unchanged by
     * {@link #content()}. This is useful for representing literal text without
     * any computation, and it never throws an
     * {@link InvariantViolation}.
     */
    final class Of implements Text {

        /**
         * The wrapped string value.
         */
        private final String x;

        /**
         * Creates a text value from the given string.
         *
         * @param x the string to wrap
         */
        public Of(final String x) {
            this.x = x;
        }

        /**
         * Returns the wrapped string value.
         *
         * @return the text content
         */
        @Override
        public String content() {
            return this.x;
        }
    }
}
