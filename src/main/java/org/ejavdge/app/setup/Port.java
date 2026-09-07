package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

public final class Port implements Num {
    private final Num origin;

    public Port() {
        this(
            new ValueOf(
                new VarOfDotenv("PORT")
            )
        );
    }

    public Port(final Text d, final Text f) {
        this(
            new ValueOf(
                new VarOfDotenv(
                    new Text.Of("PORT"),
                    d, f
                )
            )
        );
    }

    public Port(final Text t) {
        this(
            new NumOfText(
                new NonEmpty(t)
            )
        );
    }

    public Port(final Num n) {
        this.origin = new NumAbout(
            "ejudge port",
            new Positive(n)
        );
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
