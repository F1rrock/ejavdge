package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class PayloadOf implements Text {
    private final Text origin;

    public PayloadOf(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        final String full = this.origin.content();
        final int idx = full.indexOf("\r\n\r\n");
        if (idx == -1) {
            return full;
        }
        return full.substring(idx + 4);
    }
}
