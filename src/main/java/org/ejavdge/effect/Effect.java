package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

/**
 * A side effect that can be performed against the environment.
 * <p>
 * An {@code Effect} represents an action that causes some change in the
 * environment, such as sending a request to a contest system, writing to an
 * output, or waiting for a condition to become true. Unlike a pure function,
 * performing an effect may have observable consequences.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #perform()}. It is used throughout the application to encapsulate
 * actions that can be composed, deferred, or executed on demand. For example,
 * application entry points delegate their work to an {@code Effect}, and
 * decorators such as {@link org.ejavdge.effect.WithDelay} or
 * {@link org.ejavdge.effect.WithTimeout} wrap other effects to add timeout behavior.
 * <p>
 * Implementations should be idempotent where possible, but this is not
 * required. If an invariant is violated during execution, an
 * {@link InvariantViolation} is thrown to signal that the effect could not be
 * completed correctly.
 */
@FunctionalInterface
public interface Effect {

    /**
     * Performs this effect, causing its side effects to occur.
     *
     * @throws InvariantViolation if an invariant is violated during execution,
     *         indicating that the effect could not be completed correctly
     */
    void perform() throws InvariantViolation;
}
