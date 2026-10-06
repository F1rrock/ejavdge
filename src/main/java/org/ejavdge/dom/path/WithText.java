package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements whose direct text content exactly
 * matches a specified string.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[text() = '<value>']}, where {@code <base>} is the string
 * representation of the base path and {@code <value>} is the given text. This
 * selects elements matched by the base path that have a direct child text node
 * equal to the specified value.
 * <p>
 * Note that this path performs an exact match on the text of a single text
 * node. It does not consider concatenated descendant text, nor does it match
 * elements whose text differs only by surrounding whitespace. For more
 * flexible text-based selection, consider using {@link InnerText} or a custom
 * predicate.
 */
public final class WithText implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements whose direct text content equals the
     * given string, constrained to the nodes matched by the given base path.
     *
     * @param s the text value to match
     * @param p the base path constraining the search
     */
    public WithText(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that matches elements whose direct text content equals the
     * given text, constrained to the nodes matched by the given base path.
     * <p>
     * The generated XPath expression is
     * {@code <base>[text() = '<value>']}, where {@code <base>} is the view of
     * the base path and {@code <value>} is the provided text.
     *
     * @param t the text value to match
     * @param p the base path constraining the search
     */
    public WithText(final Text t, final DocPath p) {
        this(
            new Stencil(
                new Text.Of("%s[text() = '%s']"),
                new TextOfPath(p),
                t
            )
        );
    }

    /**
     * Creates a path from the given text, which is expected to contain a valid
     * XPath expression.
     *
     * @param t the text representing the XPath expression
     */
    public WithText(final Text t) {
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
