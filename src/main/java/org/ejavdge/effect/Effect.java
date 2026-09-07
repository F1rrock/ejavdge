package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

@FunctionalInterface
public interface Effect {
    void perform() throws InvariantViolation;
}
