package org.ejavdge.domain;

import org.ejavdge.error.InvariantViolation;

/**
 * A contract for a verdict, representing the outcome of an operation that can
 * either succeed or fail.
 * <p>
 * In the ejudge contest system, a verdict is used to express whether a solution
 * passed certain tests, whether a run's report is ready, or whether some other
 * condition holds. Implementations of this interface provide a single method,
 * {@link #ok()}, which returns {@code true} if the operation succeeded and
 * {@code false} otherwise.
 * <p>
 * This is a functional interface whose functional method is {@link #ok()}.
 * It is typically used with the {@link org.ejavdge.scalar.num.Fallback} or
 * conditional text constructs, or as a building block for effects like
 * {@link org.ejavdge.domain.report.AffirmingOf} and
 * {@link org.ejavdge.domain.report.AwaitingOf}.
 */
@FunctionalInterface
public interface Verdict {

    /**
     * Returns whether the operation associated with this verdict succeeded.
     *
     * @return {@code true} if the operation succeeded, {@code false} otherwise
     * @throws InvariantViolation if an invariant is violated while evaluating
     *         the verdict
     */
    boolean ok() throws InvariantViolation;
}
