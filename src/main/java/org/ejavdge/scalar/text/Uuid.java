package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.UUID;

public final class Uuid implements Text {
    private final Text origin;

    public Uuid() {
        this(UUID.randomUUID().toString());
    }

    public Uuid(final String s) {
        this(new Text.Of(s));
    }

    public Uuid(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
