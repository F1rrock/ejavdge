package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that extracts the {@code href} attributes of all anchor
 * ({@code <a>}) elements within the nodes matched by a given base path.
 * <p>
 * The resulting XPath expression is of the form
 * {@code string-join(<base>//a/@href, '<separator>')}, where {@code <base>} is
 * the string representation of the base path restricted to {@code <a>}
 * elements and {@code <separator>} is the text used to join the individual
 * link values.
 * <p>
 * This is useful for collecting all hyperlinks found in a section of a
 * document, with control over how the links are joined together.
 */
public final class LinksOnly implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that extracts the {@code href} attributes of anchor
     * elements within the given base path, joining them with a newline
     * character.
     *
     * @param p the base document path whose anchor links are to be extracted
     */
    public LinksOnly(final DocPath p) {
        this(new Text.Of("\n"), p);
    }

    /**
     * Creates a path that extracts the {@code href} attributes of anchor
     * elements within the given base path, joining them with the specified
     * separator.
     * <p>
     * The base path is first restricted to {@code <a>} elements via
     * {@link OnlyTag}, and the resulting XPath expression is
     * {@code string-join(<base>//a/@href, '<separator>')}.
     *
     * @param t the separator text used to join the link values
     * @param p the base document path whose anchor links are to be extracted
     */
    public LinksOnly(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("string-join(%s//@href, '%s')"),
            new TextOfPath(
                new OnlyTag("a", p)
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
