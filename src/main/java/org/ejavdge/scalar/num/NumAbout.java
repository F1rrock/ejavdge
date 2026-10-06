package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Num} decorator that attaches a descriptive subject to a numeric
 * value, used to enrich error messages.
 * <p>
 * This class wraps another {@link Num} instance along with a short textual
 * subject (such as {@code "problem's id"} or {@code "attempts"}). When the
 * underlying value's {@link Num#value()} method throws an
 * {@link InvariantViolation}, this decorator catches it and rethrows a new
 * {@code InvariantViolation} whose message identifies the subject, with the
 * original exception attached as the cause.
 * <p>
 * This makes it easier to diagnose failures when several numeric values are
 * being computed, by pointing to which one failed.
 */
public final class NumAbout implements Num {

    /**
     * A short description of the subject represented by this numeric value.
     */
    private final String subject;

    /**
     * The underlying numeric value whose computation is decorated with the
     * subject.
     */
    private final Num origin;

    /**
     * Creates a new numeric value decorator with the given subject and origin.
     *
     * @param s the descriptive subject used in error messages
     * @param n the underlying numeric value
     */
    public NumAbout(final String s, final Num n) {
        this.subject = s;
        this.origin = n;
    }

    /**
     * Returns the value of the underlying numeric source, enriching any
     * {@link InvariantViolation} with the configured subject.
     * <p>
     * If {@link Num#value()} on the underlying source succeeds, its result is
     * returned unchanged. If it throws an {@code InvariantViolation}, that
     * exception is wrapped in a new {@code InvariantViolation} whose message
     * includes the subject, with the original exception as the cause.
     *
     * @return the numeric value of the underlying source
     * @throws InvariantViolation if the underlying source fails to produce a
     *         value, with the subject included in the message
     */
    @Override
    public int value() throws InvariantViolation {
        try {
            return this.origin.value();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                "problem with %s\n".formatted(this.subject),
                err
            );
        }
    }
}
