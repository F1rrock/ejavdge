package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

public final class SendingOf implements Effect {
    private final Envelope src;

    public SendingOf(final Envelope e) {
        this.src = e;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.src.send();
    }
}
