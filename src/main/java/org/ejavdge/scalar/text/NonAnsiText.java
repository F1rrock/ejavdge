package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class NonAnsiText implements Text {
    private final Text origin;

    public NonAnsiText(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin
            .content()
            .replaceAll("\u001B\\[[0-?]*[ -/]*[@-~]", "");
    }
}
