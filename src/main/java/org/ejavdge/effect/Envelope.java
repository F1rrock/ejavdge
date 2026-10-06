package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

/**
 * A contract for a sendable request or payload.
 * <p>
 * An {@code Envelope} represents an action that can be sent to the environment,
 * such as submitting a form, sending a solution, or performing any other
 * request to the contest system. Implementations encapsulate the details of
 * how the payload is constructed and transmitted.
 * <p>
 * This is a functional interface whose functional method is {@link #send()}.
 * It is used throughout the application to represent operations that are
 * performed for their side effects, typically as part of a larger effect
 * pipeline.
 */
@FunctionalInterface
public interface Envelope {

    /**
     * Sends this envelope, performing the associated request or action.
     *
     * @throws InvariantViolation if an invariant is violated during sending,
     *         indicating that the request could not be completed correctly
     */
    void send() throws InvariantViolation;
}
