package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements nested within a specified tag.
 * <p>
 * This class is a convenience wrapper around {@link NestedTags} that allows
 * creating a path from a single tag name (or tag path) without explicitly
 * constructing an {@link Items} collection. It selects all elements that are
 * descendants of elements carrying the given tag name, optionally constrained
 * by a base path.
 * <p>
 * When a {@link DocPath} is passed directly, it is used as the underlying path
 * without modification.
 */
public final class NestedTag implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that selects elements nested within the tag with the given
     * name.
     *
     * @param s the tag name to look for
     * @param p the base path constraining the search
     */
    public NestedTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that selects elements nested within the tag represented by
     * the given text.
     *
     * @param t the tag name to look for
     * @param p the base path constraining the search
     */
    public NestedTag(final Text t, final DocPath p) {
        this(new NestedTags(new Items.Of<>(t), p));
    }

    /**
     * Creates a path that wraps the given document path directly.
     *
     * @param p the underlying document path
     */
    public NestedTag(final DocPath p) {
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
