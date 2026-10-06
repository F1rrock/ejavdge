package org.ejavdge.app;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;

/**
 * An effect that runs a given application.
 * <p>
 * This class adapts an {@link App} to the {@link Effect} interface by
 * delegating the {@link Effect#perform()} call to the application's
 * {@link App#run()} method.
 */
public final class RunningOf implements Effect {

    /**
     * The application to be run by this effect.
     */
    private final App src;

    /**
     * Creates a new effect that will run the given application.
     *
     * @param a the application to run
     */
    public RunningOf(final App a) {
        this.src = a;
    }

    /**
     * Performs this effect by running the underlying application.
     *
     * @throws InvariantViolation if an invariant is violated during execution
     */
    @Override
    public void perform() throws InvariantViolation {
        this.src.run();
    }
}
