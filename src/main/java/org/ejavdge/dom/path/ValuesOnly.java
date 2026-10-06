package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that extracts the {@code value} attributes of all elements
 * within the nodes matched by a given base path.
 * <p>
 * The resulting XPath expression is of the form
 * {@code string-join(<base>//@value, '<separator>')}, where {@code <base>} is
 * the string representation of the base path and {@code <separator>} is the
 * text used to join the individual attribute values.
 * <p>
 * This is useful for collecting the {@code value} attributes of form elements
 * such as {@code <input>}, {@code <option>}, or {@code <button>} found in a
 * section of a document.
 */
public final class ValuesOnly implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that extracts the {@code value} attributes of elements
     * within the given base path, joining them with a newline character.
     *
     * @param p the base document path whose {@code value} attributes are to be
     *          extracted
     */
    public ValuesOnly(final DocPath p) {
        this(new Text.Of("\n"), p);
    }

    /**
     * Creates a path that extracts the {@code value} attributes of elements
     * within the given base path, joining them with the specified separator.
     * <p>
     * The generated XPath expression is
     * {@code string-join(<base>//@value, '<separator>')}, where {@code <base>}
     * is the view of the given base path and {@code <separator>} is the
     * provided separator text.
     *
     * @param t the separator text used to join the attribute values
     * @param p the base document path whose {@code value} attributes are to be
     *          extracted
     */
    public ValuesOnly(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("string-join(%s//@value, '%s')"),
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
