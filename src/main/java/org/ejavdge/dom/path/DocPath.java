package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;

/**
 * A contract for XPath expression fragments used to select nodes in XML or HTML
 * documents.
 * <p>
 * Implementations of this interface produce a string representation of an XPath
 * expression when {@link #view()} is called. This allows XPath expressions to be
 * constructed programmatically from smaller, composable pieces.
 * <p>
 * This is a functional interface whose functional method is {@link #view()}.
 */
@FunctionalInterface
public interface DocPath {

    /**
     * Returns the XPath expression represented by this path.
     *
     * @return the XPath expression as a string
     * @throws InvariantViolation if an invariant is violated while building the
     *         expression
     */
    String view() throws InvariantViolation;

    /**
     * A simple {@link DocPath} implementation that wraps a constant string as
     * an XPath expression.
     * <p>
     * This class is useful when a literal XPath expression is needed without any
     * dynamic construction. The provided string is returned unchanged by
     * {@link #view()}.
     */
    final class Of implements DocPath {

        /**
         * The XPath expression as a string.
         */
        private final String src;

        /**
         * Creates a new path from the given string, which is expected to be a
         * valid XPath expression.
         *
         * @param s the XPath expression as a string
         */
        public Of(final String s) {
            this.src = s;
        }

        /**
         * Returns the wrapped XPath expression.
         *
         * @return the XPath expression as a string
         * @throws InvariantViolation if an invariant is violated
         */
        @Override
        public String view() throws InvariantViolation {
            return this.src;
        }
    }
}
