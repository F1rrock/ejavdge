package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.regex.Pattern;

/**
 * A {@link Text} that extracts a substring from the underlying text using a
 * regular expression.
 * <p>
 * This class implements {@link Text} and wraps a source text and a regular
 * expression pattern. When {@link #content()} is called, the pattern is
 * compiled and applied to the source text. If a match is found, the matched
 * substring is returned. If no match is found, an {@link InvariantViolation}
 * is thrown with a message that can be customized via the constructor.
 * <p>
 * This is useful for extracting a specific fragment from a larger text, such
 * as an identifier, token, or numeric value embedded in an HTML page or a
 * report. The extraction is lazy: the matching is performed only when
 * {@code content()} is invoked. The pattern is matched using
 * {@link java.util.regex.Matcher#find()}, so it searches for the first
 * occurrence of the pattern anywhere in the text rather than requiring a
 * full match.
 */
public final class Match implements Text {

    /**
     * The source text from which the match is extracted.
     */
    private final Text origin;

    /**
     * The regular expression pattern used to find the desired substring.
     */
    private final Text regex;

    /**
     * The message to include in the exception if no match is found.
     */
    private final Text message;

    /**
     * Creates a match extractor with a default error message.
     * <p>
     * The default error message is constructed by concatenating the prefix
     * {@code "Text does not match regex: "} with the regular expression's
     * content.
     *
     * @param t the source text to search
     * @param r the regular expression pattern to match
     */
    public Match(final Text t, final Text r) {
        this(
            t, r,
            new Concat(
                new Text.Of("Text does not match regex: "),
                r
            )
        );
    }

    /**
     * Creates a match extractor with a custom error message.
     *
     * @param t the source text to search
     * @param r the regular expression pattern to match
     * @param m the message to use if no match is found
     */
    public Match(final Text t, final Text r, final Text m) {
        this.origin = t;
        this.regex = r;
        this.message = m;
    }

    /**
     * Returns the first substring of the source text that matches the regular
     * expression.
     * <p>
     * The pattern is compiled from the regular expression's content and applied
     * to the source text's content. If a match is found, the matched substring
     * is returned. If no match is found, an {@link InvariantViolation} is
     * thrown with the configured error message.
     *
     * @return the matched substring
     * @throws InvariantViolation if no match is found or if an invariant is
     *         violated while retrieving the source text, the pattern, or the
     *         message
     */
    @Override
    public String content() throws InvariantViolation {
        final var r = this.regex.content();
        final var matcher = Pattern
            .compile(r)
            .matcher(this.origin.content());
        if (!matcher.find()) {
            throw new InvariantViolation(
                this.message.content()
            );
        }
        return matcher.group();
    }
}
