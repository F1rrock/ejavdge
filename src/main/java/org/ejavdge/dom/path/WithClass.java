package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements carrying a specific CSS class.
 * <p>
 * This class is a convenience wrapper around {@link WithClasses} for the common
 * case of a single class name. It matches elements whose {@code class}
 * attribute contains the given class, optionally constrained to the nodes
 * matched by a base path.
 * <p>
 * When a {@link DocPath} is passed directly, it is used as the underlying path
 * without any class-based filtering.
 */
public final class WithClass implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that matches elements with the given CSS class, searching
     * across all nodes.
     *
     * @param s the CSS class name to match
     */
    public WithClass(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a path that matches elements with the CSS class represented by
     * the given text, searching across all nodes.
     *
     * @param t the CSS class name to match
     */
    public WithClass(final Text t) {
        this(
            new WithClasses(
                new Items.Of<>(t)
            )
        );
    }

    /**
     * Creates a path that matches elements with the given CSS class,
     * constrained to the nodes matched by the given base path.
     *
     * @param s the CSS class name to match
     * @param p the base path constraining the search
     */
    public WithClass(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that matches elements with the CSS class represented by
     * the given text, constrained to the nodes matched by the given base path.
     *
     * @param t the CSS class name to match
     * @param p the base path constraining the search
     */
    public WithClass(final Text t, final DocPath p) {
        this(
            new WithClasses(
                new Items.Of<>(t),
                p
            )
        );
    }

    /**
     * Creates a path that wraps the given document path directly.
     *
     * @param p the underlying document path
     */
    public WithClass(final DocPath p) {
        this.origin = p;
    }

    /**
     * Returns the XPath expression represented by this path.
     *
     * @return the XPath expression as a string
     * @throws InvariantViolation if an invariant is violated while building the
     *         expression
     */
    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
