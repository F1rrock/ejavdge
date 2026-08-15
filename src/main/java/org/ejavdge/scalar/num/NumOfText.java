package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class NumOfText implements Num {
    private final Text src;

    public NumOfText(final String s) {
        this(new Text.Of(s));
    }

    public NumOfText(final Text t) {
        this.src = t;
    }

    @Override
    public int value() throws InvariantViolation {
        final var s = this.src.content();
        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException e) {
            throw new InvariantViolation(
                s + " is not a valid number.\n" + e.getMessage()
            );
        }
    }
}
