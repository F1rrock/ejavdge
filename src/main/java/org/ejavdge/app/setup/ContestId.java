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

public final class ContestId implements Num {
    private final Num origin;

    public ContestId() {
        this(
            new ValueOf(
                new VarOfDotenv("CONTEST_ID")
            )
        );
    }

    public ContestId(final Text d, final Text f) {
        this(
            new ValueOf(
                new VarOfDotenv(
                    new Text.Of("CONTEST_ID"),
                    d, f
                )
            )
        );
    }

    public ContestId(final Text t) {
        this(
            new NumOfText(
                new NonEmpty(t)
            )
        );
    }

    public ContestId(final Num n) {
        this.origin = new NumAbout(
            "ejudge contest id",
            new Positive(n)
        );
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
