package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.*;

/**
 * A {@link Bytes} decorator that verifies the HTTP status code of a response
 * against an expected value.
 * <p>
 * This class is used to assert that a response obtained from the contest system
 * has a particular HTTP status — for example, {@code 200} for a successful
 * page fetch or {@code 302} for a login redirect. When {@link #content()} is
 * called, the underlying response bytes are materialized (and memoized via
 * {@link Memo} to avoid re-reading), the actual status code is extracted using
 * {@link Status}, and it is compared with the expected value:
 * <ul>
 *   <li>if the status codes match, the raw response bytes are returned;</li>
 *   <li>otherwise, an {@link InvariantViolation} is thrown, using a message
 *       that combines a user-supplied hint (if any) with a default description
 *       of the mismatch (expected vs. actual status).</li>
 * </ul>
 * <p>
 * The user-supplied message is wrapped in {@link NonEmpty}, so passing an empty
 * text is equivalent to passing no message at all. When no custom message is
 * provided, the constructor defaults to {@link Empty}, and the default
 * description of the mismatch is used.
 * <p>
 * This decorator is used throughout the application wherever a specific HTTP
 * status is expected, such as in {@link org.ejavdge.auth.Session} (login
 * expects {@code 302}) and {@link org.ejavdge.contest.ContestForm} (submission
 * expects {@code 302}), ensuring that unexpected statuses are reported as
 * explicit invariant violations rather than being silently accepted.
 */
public final class HasStatus implements Bytes {

    /**
     * The underlying response bytes whose status is being verified.
     */
    private final Bytes origin;

    /**
     * The expected HTTP status code.
     */
    private final Num expected;

    /**
     * An optional user-supplied message describing the expectation.
     */
    private final Text message;

    /**
     * Creates a status verifier with the given expected status and no custom
     * message.
     * <p>
     * If the actual status does not match the expected one, the default
     * description of the mismatch (expected vs. actual) is used in the thrown
     * exception.
     *
     * @param n  the expected HTTP status code
     * @param bs the underlying response bytes
     */
    public HasStatus(final Num n, final Bytes bs) {
        this(n, new Empty(), bs);
    }

    /**
     * Creates a status verifier with the given expected status and a custom
     * message.
     * <p>
     * The supplied message is required to be non-empty via {@link NonEmpty};
     * passing an empty text is equivalent to passing no message. When the actual
     * status does not match the expected one, the message is used as a fallback
     * to the default description of the mismatch (expected vs. actual).
     *
     * @param n  the expected HTTP status code
     * @param t  a custom message describing the expectation, used if the status
     *           does not match
     * @param bs the underlying response bytes
     */
    public HasStatus(final Num n, final Text t, final Bytes bs) {
        this.origin = bs;
        this.expected = n;
        this.message = new NonEmpty(t);
    }

    /**
     * Returns the raw response bytes if the actual HTTP status matches the
     * expected one, or throws an {@link InvariantViolation} otherwise.
     * <p>
     * The underlying bytes are memoized, so the response is read at most once
     * even if it is inspected multiple times. The expected and actual status
     * codes are materialized, and their values are compared:
     * <ul>
     *   <li>if they are equal, the memoized response bytes are returned;</li>
     *   <li>otherwise, an {@link InvariantViolation} is thrown. Its message is
     *       taken from the user-supplied message if it is non-empty, or from a
     *       default description that includes both the expected and actual
     *       status codes.</li>
     * </ul>
     *
     * @return the raw response bytes, if the status matches
     * @throws InvariantViolation if the actual status does not match the
     *         expected one, or if the status code cannot be determined
     */
    @Override
    public byte[] content() throws InvariantViolation {
        final var bs = new Memo(this.origin);
        final int e = this.expected.value();
        final int a = new Status(bs).value();
        if (e == a) {
            return bs.content();
        }
        throw new InvariantViolation(
            new Fallback(
                this.message,
                new Concat(
                    new Text.Of(" "),
                    new Items.Of<>(
                        new Text.Of("Expected status"),
                        new TextOfNum(e),
                        new Text.Of("but got"),
                        new TextOfNum(a)
                    )
                )
            ).content()
        );
    }
}
