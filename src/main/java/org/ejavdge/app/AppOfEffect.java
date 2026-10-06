package org.ejavdge.app;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;

/**
 * An {@link App} that performs a given effect.
 *
 * <p>Bridges the two layers of the project: scenarios and other
 * effects in {@code org.ejavdge.effect} do not implement {@code App}
 * themselves, and apps do not perform effects directly. This class
 * adapts one to the other, so that any effect can be used where an
 * app is expected — for example, inside a {@code RunningOf} or at
 * the top of an action.
 *
 * <p>It does not add output, formatting, or success messages. The
 * effect runs as is, and any {@link InvariantViolation} it raises
 * propagates to the caller.
 */
public final class AppOfEffect implements App {
    private final Effect src;

    /**
     * @param e the effect to run when this app is run
     */
    public AppOfEffect(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
