package org.ejavdge.scalar.text.palette;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Text} decorator that renders its content in green using ANSI escape
 * codes.
 * <p>
 * This class wraps another {@link Text} instance and surrounds its content with
 * the ANSI escape sequences for green foreground color ({@code \u001B[32m})
 * and reset ({@code \u001B[0m}). When {@link #content()} is called, the
 * underlying text is materialized and wrapped with these codes, producing a
 * string that terminals supporting ANSI colors will display in green.
 * <p>
 * This is useful for adding visual emphasis to textual output, such as
 * highlighting successful results or positive messages.
 */
public final class Green implements Text {

    /**
     * The underlying text with ANSI green color codes applied.
     */
    private final Text origin;

    /**
     * Creates a green-colored view over the given text.
     * <p>
     * The text is wrapped with the ANSI escape sequences for green foreground
     * color and reset.
     *
     * @param t the text to render in green
     */
    public Green(final Text t) {
        this.origin = new Concat(
            new Text.Of("\u001B[32m"),
            t,
            new Text.Of("\u001B[0m")
        );
    }

    /**
     * Returns the text content wrapped in ANSI green color codes.
     *
     * @return the green-colored text as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
