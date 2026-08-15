package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.Locale;

public final class Lowers implements Text {
    private final Text origin;

    public Lowers(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content().toLowerCase(Locale.ROOT);
    }
}
