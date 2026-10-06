package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements nested within any of the specified
 * tags, optionally constrained by a base path.
 * <p>
 * The resulting path matches all descendant elements of the nodes matched by
 * the base path that carry one of the given tag names. This is achieved by
 * first selecting all descendants of the base path via {@link ChildrenOf} and
 * then restricting them to elements with the desired tag names using
 * {@link OnlyTags}.
 * <p>
 * When constructed with a single {@link DocPath}, that path is used directly
 * without any tag-based filtering.
 */
public final class NestedTags implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that selects descendant elements of the given base path
     * that match any of the specified tag names.
     *
     * @param ts the tag names to match
     * @param p  the base path whose descendants are searched
     */
    public NestedTags(final Items<Text> ts, final DocPath p) {
        this(
            new OnlyTags(
                ts,
                new ChildrenOf(p)
            )
        );
    }

    /**
     * Creates a path that wraps the given document path directly.
     *
     * @param p the underlying document path
     */
    public NestedTags(final DocPath p) {
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
