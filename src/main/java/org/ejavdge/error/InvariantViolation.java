package org.ejavdge.error;

/**
 * An exception thrown when an invariant of the application is violated.
 * <p>
 * This exception signals that a condition which is expected to always hold
 * (an invariant) has been broken. It is typically thrown when:
 * <ul>
 *   <li>a method receives arguments that do not satisfy its preconditions;</li>
 *   <li>the internal state of an object becomes inconsistent;</li>
 *   <li>an effect cannot be performed correctly due to an unexpected
 *       situation;</li>
 *   <li>an expected result or status (such as an HTTP status code or a
 *       verdict) is not obtained.</li>
 * </ul>
 * <p>
 * The class extends {@link IllegalArgumentException}, since invariant
 * violations are often caused by invalid inputs or state, but it is used more
 * broadly across the application to represent any unrecoverable inconsistency.
 * <p>
 * All effects in {@link org.ejavdge.effect} may throw this exception to
 * indicate that they could not complete successfully.
 */
public class InvariantViolation extends IllegalArgumentException {

    /**
     * Creates a new invariant violation with the given detail message.
     *
     * @param message the detail message describing the violated invariant
     */
    public InvariantViolation(final String message) {
        super(message);
    }

    /**
     * Creates a new invariant violation with the given detail message and
     * cause.
     *
     * @param message the detail message describing the violated invariant
     * @param cause   the underlying cause of the violation, which may be
     *                {@code null}
     */
    public InvariantViolation(final String message, final Throwable cause) {
        super(message, cause);
    }
}
