package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;

public final class OnlyAt implements DocPath {
    private final Text src;

    public OnlyAt(final Num n, final DocPath p) {
        this(
            new Stencil(
                new Text.Of("%s[%s]"),
                new TextOfPath(p),
                new TextOfNum(
                    new Positive(n)
                )
            )
        );
    }

    public OnlyAt(final Text t) {
        this.src = t;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
