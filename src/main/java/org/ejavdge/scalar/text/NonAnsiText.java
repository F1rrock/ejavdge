package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} decorator that removes ANSI escape sequences from its content.
 * <p>
 * This class wraps another {@link Text} instance and, when {@link #content()}
 * is called, returns the underlying string with all ANSI escape sequences
 * stripped out. ANSI escape sequences are typically used for terminal
 * formatting, such as setting text color, background color, or style. They are
 * represented by a special character sequence starting with the escape
 * character ({@code \u001B}) followed by a bracket and various parameters.
 * <p>
 * This is useful when text that may contain ANSI codes (for example, from
 * {@link org.ejavdge.scalar.text.palette.Red} or
 * {@link org.ejavdge.scalar.text.palette.Green}) needs to be processed or
 * displayed in an environment that does not support terminal colors, such as a
 * log file or a plain text report.
 * <p>
 * The removal is performed using the regular expression
 * {@code "\u001B\\[[0-?]*[ -/]*[@-~]"} applied via
 * {@link String#replaceAll(String, String)}. This pattern matches the standard
 * form of ANSI escape sequences and replaces them with an empty string.
 */
public final class NonAnsiText implements Text {

    /**
     * The underlying text whose content may contain ANSI escape sequences.
     */
    private final Text origin;

    /**
     * Creates a non-ANSI view over the given text.
     *
     * @param t the text whose ANSI escape sequences should be removed
     */
    public NonAnsiText(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the content of the underlying text with all ANSI escape
     * sequences removed.
     * <p>
     * The underlying content is materialized via {@link Text#content()}, and
     * every ANSI escape sequence is replaced with an empty string using the
     * regular expression {@code "\u001B\\[[0-?]*[ -/]*[@-~]"}.
     *
     * @return the text content with ANSI escape sequences stripped
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin
            .content()
            .replaceAll("\u001B\\[[0-?]*[ -/]*[@-~]", "");
    }
}
