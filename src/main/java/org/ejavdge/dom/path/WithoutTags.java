package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements which are not associated with any of
 * the specified tag names.
 * <p>
 * Unlike a simple tag-exclusion filter, this path excludes not only elements
 * whose own local name matches one of the given tags but also their
 * descendants. It does so by using the {@code ancestor-or-self} axis: an
 * element is selected only if neither it nor any of its ancestors has a local
 * name equal to one of the specified tags.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[not(<tagCond>)]}, where {@code <base>} is the string
 * representation of the base path and {@code <tagCond>} is the disjunction of
 * {@code ancestor-or-self::*[local-name() = '<tag>']} predicates for each
 * non-empty tag.
 * <p>
 * When no base path is provided, {@link AllNodes} is used, so the filter is
 * applied to the entire document.
 */
public final class WithoutTags implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements not associated with any of the given
     * tag names, searching across all nodes.
     *
     * @param ts the tag names to exclude
     */
    public WithoutTags(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path that matches elements not associated with any of the given
     * tag names, constrained to the nodes matched by the given base path.
     * <p>
     * An element is selected if neither it nor any of its ancestors has a local
     * name equal to one of the specified tags. The generated XPath expression
     * is {@code <base>[not(<tagCond>)]}, where {@code <tagCond>} is the
     * disjunction of
     * {@code ancestor-or-self::*[local-name() = '<tag>']} predicates for each
     * non-empty tag.
     *
     * @param ts the tag names to exclude
     * @param p  the base path constraining the search
     */
    public WithoutTags(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[not(%s)]"),
            new TextOfPath(p),
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "ancestor-or-self::*[local-name() = '%s']"
                        ),
                        t
                    ),
                    new Map<>(
                        NonEmpty::new,
                        new Populated<>(ts)
                    )
                )
            )
        );
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
        return this.src.content();
    }
}
