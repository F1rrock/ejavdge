package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that selects all descendant elements of the nodes matched
 * by a given base path.
 * <p>
 * The resulting XPath expression is of the form {@code <base>//*}, where
 * {@code <base>} is the string representation of the given document path.
 * This selects every element at any depth below the context nodes matched by
 * the base path.
 * <p>
 * Despite the name, this path does not restrict itself to immediate children;
 * it includes all descendant elements.
 */
public final class ChildrenOf implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that selects all descendant elements of the nodes matched
     * by the given base path.
     *
     * @param p the base document path whose descendants are to be selected
     */
    public ChildrenOf(final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s//*"),
            new TextOfPath(p)
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
