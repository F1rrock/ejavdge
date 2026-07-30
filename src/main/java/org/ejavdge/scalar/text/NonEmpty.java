package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class NonEmpty implements Text {
    private final Text origin;

    public NonEmpty(final Text x) {
        this.origin = x;
    }

    @Override
    public String content() throws InvariantViolation {
        final String x = this.origin.content();
        if (x.isEmpty()) {
            throw new InvariantViolation("Text is empty.");
        }
        return x;
    }
}
