package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;

/**
 * A {@link DocPath} that selects a specific element by its position among the
 * nodes matched by a base path.
 * <p>
 * The resulting XPath expression is of the form {@code <base>[<n>]}, where
 * {@code <base>} is the string representation of the given base path and
 * {@code <n>} is a positive integer. This selects the {@code n}-th node from
 * the set of nodes matched by the base path, using XPath's 1-based indexing.
 * <p>
 * This is useful when only a particular occurrence of an element is needed,
 * such as the first or third matching element.
 */
public final class OnlyAt implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that selects the {@code n}-th node among those matched by
     * the given base path.
     * <p>
     * The provided number is wrapped in a {@link Positive} to ensure it is
     * greater than zero. The generated XPath expression is
     * {@code <base>[<n>]}, where {@code <base>} is the base path's view and
     * {@code <n>} is the positive number.
     *
     * @param n the 1-based index of the desired node among the matches
     * @param p the base document path whose matches are indexed
     */
    public OnlyAt(final Num n, final DocPath p) {
        this(
            new Stencil(
                new Text.Of("%s[%s]"),
                new TextOfPath(p),
                new TextOfNum(
                    new Positive(n)
                )
            )
        );
    }

    /**
     * Creates a path from the given text, which is expected to contain a valid
     * XPath expression.
     *
     * @param t the text representing the XPath expression
     */
    public OnlyAt(final Text t) {
        this.src = t;
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
