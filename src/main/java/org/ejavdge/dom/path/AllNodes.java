package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects all nodes in a document.
 * <p>
 * This path corresponds to the XPath expression {@code //*}, which matches
 * every element in the document. It can be used as a base path or as a
 * catch-all selection. The underlying XPath expression can be overridden by
 * supplying a custom {@link Text} via the second constructor.
 */
public final class AllNodes implements DocPath {

    /**
     * The XPath expression represented by this path.
     */
    private final Text src;

    /**
     * Creates a path that selects all nodes, using the XPath expression
     * {@code //*}.
     */
    public AllNodes() {
        this(new Text.Of("//*"));
    }

    /**
     * Creates a path from the given text, which is expected to contain a valid
     * XPath expression.
     *
     * @param t the text representing the XPath expression
     */
    public AllNodes(final Text t) {
        this.src = t;
    }

    /**
     * Returns the XPath expression represented by this path.
     *
     * @return the XPath expression as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the expression
     */
    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
