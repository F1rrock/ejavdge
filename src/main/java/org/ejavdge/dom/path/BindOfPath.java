package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Function;

/**
 * A {@link DocPath} that composes two paths, using the result of one as the
 * input for the other.
 * <p>
 * This class allows a document path to be dynamically transformed into another
 * path at evaluation time. It holds a base {@link DocPath} and a function that
 * maps the base path's XPath expression (as a string) to a new {@link DocPath}.
 * When {@link #view()} is called, the base path is evaluated to a string, that
 * string is passed to the binding function, and the resulting path's view is
 * returned.
 * <p>
 * This is useful for building paths whose structure depends on values extracted
 * from the document, such as dynamically generated predicates.
 */
public final class BindOfPath implements DocPath {

    /**
     * The base document path whose view is used as input to the binding.
     */
    private final DocPath origin;

    /**
     * The function that transforms the base path's view into a new document
     * path.
     */
    private final Function<String, DocPath> binding;

    /**
     * Creates a new composite path from the given base path and binding
     * function.
     *
     * @param p the base document path whose string view is passed to the
     *          binding function
     * @param f the function that takes the base path's view and returns a new
     *          document path
     */
    public BindOfPath(final DocPath p, final Function<String, DocPath> f) {
        this.origin = p;
        this.binding = f;
    }

    /**
     * Returns the XPath expression represented by this composite path.
     * <p>
     * The base path is first evaluated to a string via {@link DocPath#view()}.
     * That string is then passed to the binding function, and the resulting
     * path's view is returned.
     *
     * @return the XPath expression as a string
     * @throws InvariantViolation if an invariant is violated while building the
     *         expression
     */
    @Override
    public String view() throws InvariantViolation {
        return this.binding.apply(
            this.origin.view()
        ).view();
    }
}
