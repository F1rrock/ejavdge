package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class NonEmpty implements Text {
    private final Text origin;
    private final Text message;

    public NonEmpty(final Text x) {
        this(x, new Text.Of("Text is empty."));
    }

    public NonEmpty(final Text x, final Text m) {
        this.origin = x;
        this.message = m;
    }

    @Override
    public String content() throws InvariantViolation {
        final String x = this.origin.content();
        if (x.isEmpty()) {
            throw new InvariantViolation(this.message.content());
        }
        return x;
    }
}
