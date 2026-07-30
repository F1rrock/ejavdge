package org.ejavdge.error;

public class InvariantViolation extends IllegalArgumentException {
    public InvariantViolation(final String message) {
        super(message);
    }
}
