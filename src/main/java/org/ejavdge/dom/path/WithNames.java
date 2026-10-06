package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements whose {@code name} attribute exactly
 * equals any of the specified values.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[@name = '<name1>' or @name = '<name2>' or ...]}, where
 * {@code <base>} is the string representation of the base path. Each given
 * name contributes a predicate {@code @name = '<name>'}, and the predicates are
 * combined with {@code or}.
 * <p>
 * Note that this path performs an exact match on the entire {@code name}
 * attribute. If an element has a name that is a prefix, suffix, or contains
 * extra whitespace, it will not match. For a token-based match, consider using
 * a different path.
 * <p>
 * When no base path is provided, {@link AllNodes} is used, so the name filter
 * is applied to the entire document.
 */
public final class WithNames implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements with any of the given {@code name}
     * attribute values, searching across all nodes.
     *
     * @param ts the {@code name} values to match
     */
    public WithNames(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path that matches elements with any of the given {@code name}
     * attribute values, constrained to the nodes matched by the given base
     * path.
     * <p>
     * The generated XPath expression is
     * {@code <base>[@name = '<name1>' or @name = '<name2>' or ...]}, where
     * {@code <base>} is the view of the base path. Each name contributes an
     * exact-match predicate on the {@code name} attribute.
     *
     * @param ts the {@code name} values to match
     * @param p  the base path constraining the search
     */
    public WithNames(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[%s]"),
            new TextOfPath(p),
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "@name = '%s'"
                        ),
                        t
                    ),
                    new Populated<>(ts)
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
