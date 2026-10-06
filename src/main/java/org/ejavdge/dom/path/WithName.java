package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements whose {@code name} attribute equals a
 * specified value.
 * <p>
 * This class is a convenience wrapper around {@link WithNames} for the common
 * case of a single name. It matches elements whose {@code name} attribute is
 * exactly equal to the given string, optionally constrained to the nodes
 * matched by a base path.
 * <p>
 * When a {@link DocPath} is passed directly, it is used as the underlying path
 * without any name-based filtering.
 */
public final class WithName implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that matches elements with the given {@code name}
     * attribute value, searching across all nodes.
     *
     * @param s the {@code name} value to match
     */
    public WithName(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a path that matches elements with the {@code name} attribute
     * value represented by the given text, searching across all nodes.
     *
     * @param t the {@code name} value to match
     */
    public WithName(final Text t) {
        this(
            new WithNames(
                new Items.Of<>(t)
            )
        );
    }

    /**
     * Creates a path that matches elements with the given {@code name}
     * attribute value, constrained to the nodes matched by the given base path.
     *
     * @param s the {@code name} value to match
     * @param p the base path constraining the search
     */
    public WithName(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that matches elements with the {@code name} attribute
     * value represented by the given text, constrained to the nodes matched by
     * the given base path.
     *
     * @param t the {@code name} value to match
     * @param p the base path constraining the search
     */
    public WithName(final Text t, final DocPath p) {
        this(
            new WithNames(
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
    public WithName(final DocPath p) {
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
