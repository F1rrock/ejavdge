package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * An adapter that exposes a {@link DocPath} as a {@link Text}.
 * <p>
 * This class allows an XPath expression produced by a document path to be used
 * wherever a {@link Text} is expected. The {@link #content()} method simply
 * delegates to {@link DocPath#view()} and returns the XPath expression as a
 * string.
 */
public final class TextOfPath implements Text {

    /**
     * The underlying document path.
     */
    private final DocPath src;

    /**
     * Creates a new text view over the given document path.
     *
     * @param p the document path whose XPath expression will be exposed as text
     */
    public TextOfPath(final DocPath p) {
        this.src = p;
    }

    /**
     * Returns the XPath expression of the underlying document path as a string.
     *
     * @return the XPath expression
     * @throws InvariantViolation if an invariant is violated while building the
     *         expression
     */
    @Override
    public String content() throws InvariantViolation {
        return this.src.view();
    }
}
