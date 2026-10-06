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

/**
 * The numeric id of the contest on the eJudge server.
 *
 * <p>Read from the {@code CONTEST_ID} variable in {@code .env}. When
 * a preset value and a fallback are given, the preset is used if the
 * variable is missing, and the fallback is used if the preset is
 * also missing.
 *
 * <p>The value must be a positive integer. Zero and negative numbers
 * are rejected as invariant violations.
 */
public final class ContestId implements Num {
    private final Num origin;

    /**
     * Reads {@code CONTEST_ID} from {@code .env}.
     */
    public ContestId() {
        this(
            new ValueOf(
                new VarOfDotenv("CONTEST_ID")
            )
        );
    }

    /**
     * @param d the preset to use when {@code CONTEST_ID} is missing
     * @param f the fallback to use when both are missing
     */
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

    /**
     * @param t the value to parse as a positive integer
     */
    public ContestId(final Text t) {
        this(
            new NumOfText(
                new NonEmpty(t)
            )
        );
    }

    /**
     * @param n the value to use as is, must be positive
     */
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
