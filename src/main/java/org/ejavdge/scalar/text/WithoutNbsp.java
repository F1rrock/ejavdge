package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class WithoutNbsp implements Text {
    private final Text origin;

    public WithoutNbsp(final Text text) {
        this.origin = text;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin
            .content()
            .replace('\u00A0', ' ');
    }
}
