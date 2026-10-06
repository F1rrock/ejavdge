package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;

/**
 * A {@link Bytes} decorator that retries obtaining the underlying byte content
 * when an {@link InvariantViolation} occurs.
 * <p>
 * This class wraps another {@link Bytes} instance and a number of allowed
 * attempts. When {@link #content()} is called, it tries to materialize the
 * underlying bytes. If that fails with an {@link InvariantViolation}, the
 * attempt is retried as long as there are remaining attempts. If all attempts
 * are exhausted, a new {@link InvariantViolation} is thrown that indicates the
 * failure and includes the last encountered exception as its cause.
 * <p>
 * The number of attempts is represented by a {@link Num} and is wrapped in a
 * {@link Positive} to ensure it is strictly greater than zero. The count is
 * also labelled as {@code "attempts"} via {@link NumAbout}, which enriches any
 * error message produced when the count itself cannot be evaluated.
 * <p>
 * This is useful for operations that may fail transiently, such as network
 * requests or interactions with a remote contest system, where a simple retry
 * can often succeed.
 */
public final class WithRetries implements Bytes {

    /**
     * The underlying byte sequence whose content is obtained with retries.
     */
    private final Bytes origin;

    /**
     * The maximum number of attempts to obtain the content.
     */
    private final Num attempts;

    /**
     * Creates a retrying wrapper with the given byte sequence and a fixed
     * number of attempts.
     *
     * @param bs the underlying byte sequence
     * @param n  the maximum number of attempts, must be positive
     */
    public WithRetries(final Bytes bs, final int n) {
        this(bs, new Num.Of(n));
    }

    /**
     * Creates a retrying wrapper with the given byte sequence and a numeric
     * number of attempts.
     * <p>
     * The provided count is wrapped in a {@link Positive} to ensure it is
     * greater than zero, and labelled as {@code "attempts"} for diagnostic
     * purposes.
     *
     * @param bs the underlying byte sequence
     * @param n  the maximum number of attempts, must be positive
     */
    public WithRetries(final Bytes bs, final Num n) {
        this.origin = bs;
        this.attempts = new NumAbout("attempts", new Positive(n));
    }

    /**
     * Returns the content of the underlying byte sequence, retrying on failure
     * up to the configured number of attempts.
     * <p>
     * On each invocation, the current remaining attempt count is read. The
     * underlying content is then requested via {@link Bytes#content()}. If it
     * succeeds, the resulting byte array is returned. If it throws an
     * {@link InvariantViolation}, the behavior depends on the remaining
     * attempts:
     * <ul>
     *   <li>if no attempts remain (i.e., the count is {@code 1} or less), a new
     *       {@link InvariantViolation} is thrown with the message
     *       {@code "Bytes not obtained within the allowed retry attempts.\n"}
     *       and the original exception as the cause;</li>
     *   <li>otherwise, a new {@code WithRetries} instance is created with the
     *       same underlying bytes and one fewer attempt, and its
     *       content method is invoked recursively.</li>
     * </ul>
     *
     * @return the byte content of the underlying sequence
     * @throws InvariantViolation if the content cannot be obtained within the
     *         allowed number of attempts, with the last encountered failure as
     *         the cause
     */
    @Override
    public byte[] content() throws InvariantViolation {
        final var left = this.attempts.value();
        try {
            return this.origin.content();
        } catch (final InvariantViolation e) {
            if (left <= 1) {
                throw new InvariantViolation(
                    "Bytes not obtained within the allowed retry attempts.\n",
                    e
                );
            }
            return new WithRetries(this.origin, left - 1).content();
        }
    }
}
