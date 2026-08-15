package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

public final class TextOfNum implements Text {
    private final Num src;

    public TextOfNum(final int n) {
        this(new Num.Of(n));
    }

    public TextOfNum(final Num n) {
        this.src = n;
    }

    @Override
    public String content() throws InvariantViolation {
        return String.valueOf(this.src.value());
    }
}
