package org.ejavdge.error;

public class InvariantViolation extends IllegalArgumentException {
    public InvariantViolation(final String message) {
        super(message);
    }

    public InvariantViolation(final String message, final Throwable cause) {
        super(message, cause);
    }
}
