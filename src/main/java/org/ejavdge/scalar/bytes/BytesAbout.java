package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Bytes} decorator that attaches a descriptive subject to a byte
 * sequence, used to enrich error messages.
 * <p>
 * This class wraps another {@link Bytes} instance along with a short textual
 * subject (such as {@code "ejudge session"} or {@code "contest resource"}).
 * When the underlying byte sequence's {@link Bytes#content()} method throws an
 * {@link InvariantViolation}, this decorator catches it and rethrows a new
 * {@code InvariantViolation} whose message identifies the subject, with the
 * original exception attached as the cause.
 * <p>
 * This makes it easier to diagnose failures when several byte sequences are
 * being materialized, by pointing to which one failed.
 */
public final class BytesAbout implements Bytes {

    /**
     * A short description of the subject represented by this byte sequence.
     */
    private final String subject;

    /**
     * The underlying byte sequence whose content is decorated with the subject.
     */
    private final Bytes origin;

    /**
     * Creates a new byte sequence decorator with the given subject and origin.
     *
     * @param s  the descriptive subject used in error messages
     * @param bs the underlying byte sequence
     */
    public BytesAbout(final String s, final Bytes bs) {
        this.subject = s;
        this.origin = bs;
    }

    /**
     * Returns the content of the underlying byte sequence, enriching any
     * {@link InvariantViolation} with the configured subject.
     * <p>
     * If {@link Bytes#content()} on the underlying sequence succeeds, its
     * result is returned unchanged. If it throws an {@code InvariantViolation},
     * that exception is wrapped in a new {@code InvariantViolation} whose
     * message includes the subject, with the original exception as the cause.
     *
     * @return the byte content of the underlying sequence
     * @throws InvariantViolation if the underlying sequence fails to produce
     *         its content, with the subject included in the message
     */
    @Override
    public byte[] content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                "problem with %s\n".formatted(this.subject),
                err
            );
        }
    }
}
