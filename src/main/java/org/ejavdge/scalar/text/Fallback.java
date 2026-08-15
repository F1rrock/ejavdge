package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class Fallback implements Text {
    private final Text origin;
    private final Text then;

    public Fallback(final Text t, final Text f) {
        this.origin = t;
        this.then = f;
    }

    @Override
    public String content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation e) {
            return this.then.content();
        }
    }
}
