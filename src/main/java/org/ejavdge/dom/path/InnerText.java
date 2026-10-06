package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects the inner text of all elements matched by a
 * given base path, concatenating the text nodes with a separator.
 * <p>
 * The resulting XPath expression is of the form
 * {@code string-join(<base>//text(), '<separator>')}, where {@code <base>} is
 * the string representation of the base document path and {@code <separator>}
 * is the text used to join the individual text nodes.
 * <p>
 * This is useful for extracting the human-readable text content of a set of
 * elements, with control over how the text fragments are joined together.
 */
public final class InnerText implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that selects the inner text of all elements matched by the
     * given base path, joining the text nodes with a newline character.
     *
     * @param p the base document path whose matched elements' text is selected
     */
    public InnerText(final DocPath p) {
        this(new Text.Of("\n"), p);
    }

    /**
     * Creates a path that selects the inner text of all elements matched by the
     * given base path, joining the text nodes with the specified separator.
     * <p>
     * The generated XPath expression is
     * {@code string-join(<base>//text(), '<separator>')}, where {@code <base>}
     * is the view of the given base path and {@code <separator>} is the
     * provided separator text.
     *
     * @param t the separator text used to join the text nodes
     * @param p the base document path whose matched elements' text is selected
     */
    public InnerText(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("string-join(%s//text(), '%s')"),
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
