package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.BindOfText;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * An XPath expression that selects elements which have a given set of CSS
 * classes and are not followed by sibling elements carrying those same classes.
 * <p>
 * In other words, this path matches the last element in a group of siblings
 * sharing the specified class(es). It is commonly used to select the final node
 * in a sequence of elements with the same class.
 * <p>
 * The class-matching predicate is built from the given class names using the
 * standard XPath idiom
 * {@code contains(concat(' ', normalize-space(@class), ' '), ' <class> ')}.
 * When multiple classes are provided, they are combined with {@code or}.
 * <p>
 * An optional base {@link DocPath} can be supplied to constrain the search to
 * a particular subtree; otherwise, all nodes are considered.
 */
public final class AfterClasses implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path selecting the last sibling among nodes carrying any of the
     * given classes, searching across all nodes.
     *
     * @param ts the CSS class names to match
     */
    public AfterClasses(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path selecting the last sibling among nodes carrying any of the
     * given classes, constrained to the subtree matched by the given path.
     * <p>
     * The resulting XPath matches elements that:
     * <ul>
     *   <li>carry one of the specified classes;</li>
     *   <li>have a preceding sibling that carries the same class;</li>
     *   <li>have no following sibling that carries the same class;</li>
     *   <li>are matched by the given base path {@code p}.</li>
     * </ul>
     *
     * @param ts the CSS class names to match
     * @param p  the base path constraining the search
     */
    public AfterClasses(final Items<Text> ts, final DocPath p) {
        this.src = new BindOfText(
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "contains(concat(' ', normalize-space(@class), ' '), ' %s ')"
                        ),
                        t
                    ),
                    new Populated<>(ts)
                )
            ),
            c -> new Stencil(
                new Concat(
                    "%s[preceding-sibling::*[%s]"
                        + " and "
                        + "not(following-sibling::*[%s])"
                        + " and "
                        + "not(%s)"
                        + "]"
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
