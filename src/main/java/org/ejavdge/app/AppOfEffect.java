package org.ejavdge.app;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;

public final class AppOfEffect implements App {
    private final Effect src;

    public AppOfEffect(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
