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
 * The TCP port of the eJudge server.
 *
 * <p>Read from the {@code PORT} variable in {@code .env}. When a
 * preset value and a fallback are given, the preset is used if the
 * variable is missing, and the fallback is used if the preset is
 * also missing.
 *
 * <p>The value must be a positive integer. Zero and negative numbers
 * are rejected as invariant violations. The upper bound of the TCP
 * port range (65535) is not enforced here; an out-of-range value
 * fails later, when a socket is opened.
 */
public final class Port implements Num {
    private final Num origin;

    /**
     * Reads {@code PORT} from {@code .env}.
     */
    public Port() {
        this(
            new ValueOf(
                new VarOfDotenv("PORT")
            )
        );
    }

    /**
     * @param d the preset to use when {@code PORT} is missing
     * @param f the fallback to use when both are missing
     */
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

    /**
     * @param t the value to parse as a positive integer
     */
    public Port(final Text t) {
        this(
            new NumOfText(
                new NonEmpty(t)
            )
        );
    }

    /**
     * @param n the value to use as is, must be positive
     */
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
