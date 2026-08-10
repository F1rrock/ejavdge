package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class Trimmed implements Text {
    private final Text origin;

    public Trimmed(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content().trim();
    }
}
