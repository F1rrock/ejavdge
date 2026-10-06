package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements whose {@code class} attribute exactly
 * equals any of the specified CSS class names.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[@class = '<class1>' or @class = '<class2>' or ...]}, where
 * {@code <base>} is the string representation of the base path. Each given
 * class name contributes a predicate {@code @class = '<class>'}, and the
 * predicates are combined with {@code or}.
 * <p>
 * Note that this path performs an exact match on the entire {@code class}
 * attribute. If an element carries multiple classes or additional whitespace,
 * it will not match unless the attribute value is exactly one of the specified
 * strings. For a token-based match, consider using {@link AfterClasses} or a
 * similar path.
 * <p>
 * When no base path is provided, {@link AllNodes} is used, so the class filter
 * is applied to the entire document.
 */
public final class WithClasses implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements with any of the given CSS class
     * names, searching across all nodes.
     *
     * @param ts the CSS class names to match
     */
    public WithClasses(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path that matches elements with any of the given CSS class
     * names, constrained to the nodes matched by the given base path.
     * <p>
     * The generated XPath expression is
     * {@code <base>[@class = '<class1>' or @class = '<class2>' or ...]}, where
     * {@code <base>} is the view of the base path. Each class name contributes
     * an exact-match predicate on the {@code class} attribute.
     *
     * @param ts the CSS class names to match
     * @param p  the base path constraining the search
     */
    public WithClasses(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[%s]"),
            new TextOfPath(p),
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "@class = '%s'"
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
