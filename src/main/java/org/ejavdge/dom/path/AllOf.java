package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that combines multiple document paths into a single XPath
 * expression.
 * <p>
 * The resulting expression uses the XPath {@code string-join} function to
 * concatenate the string representations of the given paths, separated by a
 * configurable delimiter. By default, the delimiter is a newline character.
 * <p>
 * This is useful when a selection needs to produce a single string that
 * aggregates the results of several XPath expressions.
 */
public final class AllOf implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a combined path from the given document paths, using a newline as
     * the default separator.
     *
     * @param ps the document paths to combine
     */
    public AllOf(final DocPath ...ps) {
        this(new Items.Of<>(ps));
    }

    /**
     * Creates a combined path from the given collection of document paths,
     * using a newline as the default separator.
     *
     * @param ps the document paths to combine
     */
    public AllOf(final Items<DocPath> ps) {
        this(new Text.Of("\n"), ps);
    }

    /**
     * Creates a combined path from the given document paths, using the
     * specified text as the separator.
     *
     * @param t  the separator text
     * @param ps the document paths to combine
     */
    public AllOf(final Text t, final DocPath ...ps) {
        this(t, new Items.Of<>(ps));
    }

    /**
     * Creates a combined path from the given collection of document paths,
     * using the specified text as the separator.
     * <p>
     * The resulting XPath expression is of the form
     * {@code string-join((path1, path2, ...), 'separator')}, where each
     * {@code pathN} is the string representation of the corresponding
     * {@link DocPath}.
     *
     * @param t  the separator text
     * @param ps the document paths to combine
     */
    public AllOf(final Text t, final Items<DocPath> ps) {
        this.src = new Stencil(
            new Text.Of("string-join((%s), '%s')"),
            new Concat(
                new Text.Of(", "),
                new Map<>(TextOfPath::new, ps)
            ),
            t
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
