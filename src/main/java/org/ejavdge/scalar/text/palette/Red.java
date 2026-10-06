package org.ejavdge.scalar.text.palette;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Text} decorator that renders its content in red using ANSI escape
 * codes.
 * <p>
 * This class wraps another {@link Text} instance and surrounds its content with
 * the ANSI escape sequences for red foreground color ({@code \u001B[31m}) and
 * reset ({@code \u001B[0m}). When {@link #content()} is called, the underlying
 * text is materialized and wrapped with these codes, producing a string that
 * terminals supporting ANSI colors will display in red.
 * <p>
 * This is useful for adding visual emphasis to textual output, such as
 * highlighting errors, warnings, or other negative messages.
 */
public final class Red implements Text {

    /**
     * The underlying text with ANSI red color codes applied.
     */
    private final Text origin;

    /**
     * Creates a red-colored view over the given text.
     * <p>
     * The text is wrapped with the ANSI escape sequences for red foreground
     * color and reset.
     *
     * @param t the text to render in red
     */
    public Red(final Text t) {
        this.origin = new Concat(
            new Text.Of("\u001B[31m"),
            t,
            new Text.Of("\u001B[0m")
        );
    }

    /**
     * Returns the text content wrapped in ANSI red color codes.
     *
     * @return the red-colored text as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
