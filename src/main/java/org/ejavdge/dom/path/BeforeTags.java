package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.*;

/**
 * A {@link DocPath} that selects elements appearing before the first occurrence
 * of the specified tags among their siblings.
 * <p>
 * The resulting XPath expression matches elements that:
 * <ul>
 *   <li>are matched by a base path;</li>
 *   <li>have a following sibling whose local name is one of the given tags;</li>
 *   <li>have no preceding sibling whose local name is one of the given tags;</li>
 *   <li>do not themselves have a local name equal to any of the given tags.</li>
 * </ul>
 * In other words, it selects all siblings that come before the first element
 * carrying one of the specified tag names.
 * <p>
 * When multiple tags are provided, the condition for a following or preceding
 * sibling is that its local name equals any of the tags, combined with
 * {@code or}. Tag names are required to be non-empty.
 */
public final class BeforeTags implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that selects elements before the first occurrence
     * of the given tags, searching across all nodes.
     *
     * @param ts the tag names to look for
     */
    public BeforeTags(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path that selects elements before the first occurrence
     * of the given tags, constrained to the subtree matched by the given base
     * path.
     * <p>
     * The generated XPath expression is of the form
     * {@code <base>[following-sibling::*[<tagCond>] and not(preceding-sibling::*[<tagCond>]) and not(<tagCond>)]},
     * where {@code <tagCond>} is the disjunction of
     * {@code local-name() = '<tag>'} for each non-empty tag.
     *
     * @param ts the tag names to look for
     * @param p  the base path constraining the search
     */
    public BeforeTags(final Items<Text> ts, final DocPath p) {
        this.src = new BindOfText(
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "local-name() = '%s'"
                        ),
                        t
                    ),
                    new Map<>(
                        NonEmpty::new,
                        new Populated<>(ts)
                    )
                )
            ),
            c -> new Stencil(
                new Concat(
                    "%s[following-sibling::*[%s]",
                    " and ",
                    "not(preceding-sibling::*[%s])",
                    " and ",
                    "not(%s)]"
                ),
                new TextOfPath(p),
                new Text.Of(c),
                new Text.Of(c),
                new Text.Of(c)
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
