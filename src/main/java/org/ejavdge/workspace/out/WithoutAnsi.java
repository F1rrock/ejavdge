package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonAnsiText;
import org.ejavdge.scalar.text.Text;

/**
 * An {@link Out} decorator that strips ANSI escape sequences from the text
 * before writing it to the decorated output.
 * <p>
 * Text that has been colorized or otherwise formatted for terminal display —
 * for example, using the wrappers in
 * {@link org.ejavdge.scalar.text.palette} or other ANSI escape codes — contains
 * special character sequences that are meaningful only when rendered by a
 * terminal that supports them. When such text is written to a destination that
 * does not support ANSI codes, those sequences would appear as literal
 * characters and clutter the output.
 * <p>
 * This decorator solves that problem by wrapping the text in a
 * {@link NonAnsiText}, which removes all ANSI escape sequences, before passing
 * it on to the underlying output. This makes it possible to use the same
 * colorized text throughout the application and simply strip the color codes at
 * the output boundary when targeting a plain-text destination.
 */
public final class WithoutAnsi implements Out {

    /**
     * The underlying output to which stripped text is written.
     */
    private final Out origin;

    /**
     * Creates a decorator that strips ANSI escape sequences before writing to
     * the given output.
     *
     * @param o the output to decorate
     */
    public WithoutAnsi(final Out o) {
        this.origin = o;
    }

    /**
     * Writes the given text to the decorated output after stripping all ANSI
     * escape sequences.
     * <p>
     * The text is first wrapped in a {@link NonAnsiText}, which removes any ANSI
     * escape sequences from its content, and the resulting plain text is then
     * passed to the underlying output's {@link Out#write(Text)} method.
     *
     * @param t the text to write
     * @throws InvariantViolation if an invariant is violated while stripping
     *         the ANSI sequences or while writing the resulting text
     */
    @Override
    public void write(final Text t) throws InvariantViolation {
        this.origin.write(new NonAnsiText(t));
    }
}
