package org.ejavdge.scalar.text;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;

public final class Notice implements Text {
    private final Effect src;
    private final Text origin;

    public Notice(final Effect e, final Text t) {
        this.src = e;
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        this.src.perform();
        return this.origin.content();
    }
}
