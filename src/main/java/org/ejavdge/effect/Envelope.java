package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

@FunctionalInterface
public interface Envelope {
    void send() throws InvariantViolation;
}
