package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements appearing before a specified tag.
 * <p>
 * This class is a convenience wrapper around {@link BeforeTags} that allows
 * creating a path from a single tag name (or tag path) without explicitly
 * constructing an {@link Items} collection. It selects all elements that
 * precede the first occurrence of the given tag in the document order.
 * <p>
 * An optional base {@link DocPath} can be supplied to constrain the search to
 * a particular subtree. When a {@link DocPath} is passed directly, it is used
 * as the underlying path without modification.
 */
public final class BeforeTag implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that selects elements before the tag with the given name.
     *
     * @param s the tag name to look for
     */
    public BeforeTag(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a path that selects elements before the tag represented by the
     * given text.
     *
     * @param t the tag name to look for
     */
    public BeforeTag(final Text t) {
        this(
            new BeforeTags(
                new Items.Of<>(t)
            )
        );
    }

    /**
     * Creates a path that selects elements before the tag with the given name,
     * constrained to the subtree matched by the given base path.
     *
     * @param s the tag name to look for
     * @param p the base path constraining the search
     */
    public BeforeTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that selects elements before the tag represented by the
     * given text, constrained to the subtree matched by the given base path.
     *
     * @param t the tag name to look for
     * @param p the base path constraining the search
     */
    public BeforeTag(final Text t, final DocPath p) {
        this(
            new BeforeTags(
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
    public BeforeTag(final DocPath p) {
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
