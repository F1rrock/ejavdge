package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class Contextual implements Text {
    private final String context;
    private final Text origin;

    public Contextual(final String context, final Text origin) {
        this.context = context;
        this.origin = origin;
    }

    @Override
    public String content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                String.format(
                    "%s: %s",
                    this.context,
                    err.getMessage()
                )
            );
        }
    }
}