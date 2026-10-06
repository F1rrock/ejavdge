package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements whose local name does not match a
 * specified tag.
 * <p>
 * This class is a convenience wrapper around {@link WithoutTags} for the common
 * case of excluding a single tag name. It matches elements that are not
 * associated with the given tag, optionally constrained to the nodes matched by
 * a base path.
 * <p>
 * When a {@link DocPath} is passed directly, it is used as the underlying path
 * without any tag-based filtering.
 */
public final class WithoutTag implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that matches elements whose local name is not the given
     * tag, searching across all nodes.
     *
     * @param s the tag name to exclude
     */
    public WithoutTag(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a path that matches elements whose local name is not the tag
     * represented by the given text, searching across all nodes.
     *
     * @param t the tag name to exclude
     */
    public WithoutTag(final Text t) {
        this(
            new WithoutTags(
                new Items.Of<>(t)
            )
        );
    }

    /**
     * Creates a path that matches elements whose local name is not the given
     * tag, constrained to the nodes matched by the given base path.
     *
     * @param s the tag name to exclude
     * @param p the base path constraining the search
     */
    public WithoutTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that matches elements whose local name is not the tag
     * represented by the given text, constrained to the nodes matched by the
     * given base path.
     *
     * @param t the tag name to exclude
     * @param p the base path constraining the search
     */
    public WithoutTag(final Text t, final DocPath p) {
        this(
            new WithoutTags(
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
    public WithoutTag(final DocPath p) {
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
