package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements which are not associated with any of
 * the specified CSS classes.
 * <p>
 * Unlike a simple class-exclusion filter, this path excludes not only elements
 * that directly carry one of the given classes but also their descendants. It
 * does so by using the {@code ancestor-or-self} axis: an element is selected
 * only if neither it nor any of its ancestors has one of the specified classes.
 * <p>
 * The class-matching predicate uses the standard XPath idiom
 * {@code contains(concat(' ', normalize-space(@class), ' '), ' <class> ')} to
 * match classes as whole tokens. When multiple classes are provided, they are
 * combined with {@code or}.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[not(<classCond>)]}, where {@code <base>} is the string
 * representation of the base path and {@code <classCond>} is the disjunction
 * of {@code ancestor-or-self::*[contains(...)]} predicates for each class.
 * <p>
 * When no base path is provided, {@link AllNodes} is used, so the filter is
 * applied to the entire document.
 */
public final class WithoutClasses implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements not associated with any of the given
     * CSS classes, searching across all nodes.
     *
     * @param ts the CSS class names to exclude
     */
    public WithoutClasses(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path that matches elements not associated with any of the given
     * CSS classes, constrained to the nodes matched by the given base path.
     * <p>
     * An element is selected if neither it nor any of its ancestors carries one
     * of the specified classes. The generated XPath expression is
     * {@code <base>[not(<classCond>)]}, where {@code <classCond>} is the
     * disjunction of
     * {@code ancestor-or-self::*[contains(concat(' ', normalize-space(@class), ' '), ' <class> ')]}
     * predicates for each given class.
     *
     * @param ts the CSS class names to exclude
     * @param p  the base path constraining the search
     */
    public WithoutClasses(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[not(%s)]"),
            new TextOfPath(p),
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Concat(
                            "ancestor-or-self::*[",
                            "contains(concat(' ', normalize-space(@class), ' '), ' %s ')",
                            "]"
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
