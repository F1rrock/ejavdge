package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that restricts a selection to elements carrying a single
 * specified tag name.
 * <p>
 * This class is a convenience wrapper around {@link OnlyTags} that allows
 * creating a path from one tag name (or tag path) without explicitly
 * constructing an {@link Items} collection. It matches only elements whose
 * local name equals the given tag, optionally constrained by a base path.
 * <p>
 * When a {@link DocPath} is passed directly, it is used as the underlying path
 * without modification.
 */
public final class OnlyTag implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that matches elements with the given tag name, searching
     * across all nodes.
     *
     * @param s the tag name to match
     */
    public OnlyTag(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a path that matches elements with the tag name represented by the
     * given text, searching across all nodes.
     *
     * @param t the tag name to match
     */
    public OnlyTag(final Text t) {
        this(
            new OnlyTags(
                new Items.Of<>(t)
            )
        );
    }

    /**
     * Creates a path that matches elements with the given tag name, constrained
     * to the nodes matched by the given base path.
     *
     * @param s the tag name to match
     * @param p the base path constraining the search
     */
    public OnlyTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that matches elements with the tag name represented by the
     * given text, constrained to the nodes matched by the given base path.
     *
     * @param t the tag name to match
     * @param p the base path constraining the search
     */
    public OnlyTag(final Text t, final DocPath p) {
        this(
            new OnlyTags(
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
    public OnlyTag(final DocPath p) {
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
