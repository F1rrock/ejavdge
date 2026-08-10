package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class NumOfHex implements Num {
    private final Text src;

    public NumOfHex(final String s) {
        this(new Text.Of(s));
    }

    public NumOfHex(final Text t) {
        this.src = t;
    }

    @Override
    public int value() throws InvariantViolation {
        final var s = this.src.content();
        try {
            return Integer.parseInt(s, 16);
        } catch (final NumberFormatException e) {
            throw new InvariantViolation(
                s + " is not a valid hex number.\n" + e.getMessage()
            );
        }
    }
}
