package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects elements which precede a sibling element with
 * a given {@code id}.
 * <p>
 * The resulting XPath expression matches nodes selected by a base path that
 * have a following sibling element whose {@code id} attribute equals the
 * specified value. In other words, it selects the elements that come before
 * the element with the given id in the document order.
 * <p>
 * This is useful for extracting content that appears before a known anchor
 * element, such as a header or a section marker identified by its id.
 */
public final class BeforeId implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that selects elements before the element with the given
     * id, using a string as the id value.
     *
     * @param s the id value to look for in a following sibling
     * @param p the base path constraining the selection
     */
    public BeforeId(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    /**
     * Creates a path that selects elements before the element with the given
     * id, using a {@link Text} as the id value.
     * <p>
     * The generated XPath expression is of the form
     * {@code <base>[following-sibling::*[@id = '<id>']]}, where {@code <base>}
     * is the string representation of the given base path and {@code <id>} is
     * the non-empty text value.
     *
     * @param t the id value to look for in a following sibling
     * @param p the base path constraining the selection
     */
    public BeforeId(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[following-sibling::*[@id = '%s']]"),
            new TextOfPath(p),
            new NonEmpty(t)
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
