package org.ejavdge.scalar.text.palette;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;

public final class Red implements Text {
    private final Text origin;

    public Red(final Text t) {
        this.origin = new Concat(
            new Text.Of("\u001B[31m"),
            t,
            new Text.Of("\u001B[0m")
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
