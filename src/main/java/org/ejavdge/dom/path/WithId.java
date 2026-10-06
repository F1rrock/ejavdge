package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements whose {@code id} attribute equals a
 * specified value.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[@id = '<id>']}, where {@code <base>} is the string
 * representation of the base path and {@code <id>} is the given identifier.
 * This selects elements matched by the base path that carry the exact
 * {@code id} attribute value.
 * <p>
 * This is useful for locating a uniquely identified element, such as a
 * container, form, or anchor, within a document or a section of it.
 */
public final class WithId implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements with the given {@code id},
     * constrained to the nodes matched by the given base path, using a string
     * as the identifier.
     *
     * @param s the {@code id} value to match
     * @param p the base path constraining the search
     */
    public WithId(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that matches elements with the given {@code id},
     * constrained to the nodes matched by the given base path, using a
     * {@link Text} as the identifier.
     * <p>
     * The generated XPath expression is
     * {@code <base>[@id = '<id>']}, where {@code <base>} is the view of the
     * base path and {@code <id>} is the provided identifier text.
     *
     * @param t the {@code id} value to match
     * @param p the base path constraining the search
     */
    public WithId(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[@id = '%s']"),
            new TextOfPath(p),
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
