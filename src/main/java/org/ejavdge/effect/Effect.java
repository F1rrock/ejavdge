package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

public interface Effect {
    void perform() throws InvariantViolation;
}
