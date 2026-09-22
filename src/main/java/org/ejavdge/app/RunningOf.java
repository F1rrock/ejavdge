package org.ejavdge.app;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;

public final class RunningOf implements Effect {
    private final App src;

    public RunningOf(final App a) {
        this.src = a;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.src.run();
    }
}
