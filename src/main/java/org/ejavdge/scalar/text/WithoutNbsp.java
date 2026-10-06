package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} decorator that replaces non-breaking spaces with regular
 * spaces.
 * <p>
 * In HTML, a non-breaking space is often represented by the entity
 * {@code &nbsp;} and the corresponding Unicode character {@code U+00A0}. While
 * this character prevents line breaks and collapses in browsers, it can be
 * inconvenient when processing extracted text — for example, when splitting
 * lines, trimming, or comparing strings. This class normalizes such text by
 * replacing every occurrence of {@code U+00A0} with a regular space
 * ({@code U+0020}).
 * <p>
 * This is particularly useful when cleaning up problem descriptions, reports,
 * or other content scraped from the contest system, where non-breaking spaces
 * are frequently used for layout and may otherwise interfere with text
 * processing.
 */
public final class WithoutNbsp implements Text {

    /**
     * The underlying text whose non-breaking spaces are replaced.
     */
    private final Text origin;

    /**
     * Creates a normalized view over the given text, replacing non-breaking
     * spaces with regular spaces.
     *
     * @param text the text to normalize
     */
    public WithoutNbsp(final Text text) {
        this.origin = text;
    }

    /**
     * Returns the content of the underlying text with all non-breaking spaces
     * replaced by regular spaces.
     * <p>
     * The underlying content is materialized via {@link Text#content()}, and
     * every occurrence of the character {@code U+00A0} is replaced with a
     * regular space ({@code U+0020}) using {@link String#replace(char, char)}.
     *
     * @return the normalized text content
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the underlying content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin
            .content()
            .replace('\u00A0', ' ');
    }
}
