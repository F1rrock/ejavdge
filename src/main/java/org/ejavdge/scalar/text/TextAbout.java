package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} decorator that attaches a descriptive subject to a text value,
 * used to enrich error messages.
 * <p>
 * This class wraps another {@link Text} instance along with a short textual
 * subject (such as {@code "problem's marker"} or {@code "main page"}). When the
 * underlying text's {@link Text#content()} method throws an
 * {@link InvariantViolation}, this decorator catches it and rethrows a new
 * {@code InvariantViolation} whose message identifies the subject, with the
 * original exception attached as the cause.
 * <p>
 * This makes it easier to diagnose failures when several text values are being
 * materialized, by pointing to which one failed.
 */
public final class TextAbout implements Text {

    /**
     * A short description of the subject represented by this text value.
     */
    private final String subject;

    /**
     * The underlying text whose content is decorated with the subject.
     */
    private final Text origin;

    /**
     * Creates a new text decorator with the given subject and origin.
     *
     * @param s the descriptive subject used in error messages
     * @param t the underlying text
     */
    public TextAbout(final String s, final Text t) {
        this.subject = s;
        this.origin = t;
    }

    /**
     * Returns the content of the underlying text, enriching any
     * {@link InvariantViolation} with the configured subject.
     * <p>
     * If {@link Text#content()} on the underlying text succeeds, its result is
     * returned unchanged. If it throws an {@code InvariantViolation}, that
     * exception is wrapped in a new {@code InvariantViolation} whose message
     * includes the subject, with the original exception as the cause.
     *
     * @return the text content of the underlying text
     * @throws InvariantViolation if the underlying text fails to produce its
     *         content, with the subject included in the message
     */
    @Override
    public String content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                """
                problem with %s
                """.formatted(this.subject),
                err
            );
        }
    }
}
