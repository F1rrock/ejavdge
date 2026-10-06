package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} decorator that trims leading and trailing whitespace from its
 * content.
 * <p>
 * This class wraps another {@link Text} instance and, when {@link #content()}
 * is called, returns the underlying string with any leading and trailing
 * whitespace removed. The trimming is performed using
 * {@link String#trim()}, which removes all characters with a code point less
 * than or equal to {@code U+0020} (the space character) from both ends of the
 * string.
 * <p>
 * This is useful for normalizing text extracted from HTML documents or other
 * sources, where values are often padded with spaces, tabs, or newlines that
 * are not meaningful to the application.
 */
public final class Trimmed implements Text {

    /**
     * The underlying text whose content is trimmed.
     */
    private final Text origin;

    /**
     * Creates a trimmed view over the given text.
     *
     * @param t the text to trim
     */
    public Trimmed(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the content of the underlying text with leading and trailing
     * whitespace removed.
     * <p>
     * The underlying content is materialized via {@link Text#content()} and
     * passed through {@link String#trim()}.
     *
     * @return the trimmed content of the underlying text
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content().trim();
    }
}
