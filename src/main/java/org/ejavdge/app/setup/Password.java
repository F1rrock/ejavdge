package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

/**
 * The password of the account used to access the eJudge contest.
 *
 * <p>Read from the {@code PASSWORD} variable in {@code .env}. When a
 * preset value and a fallback are given, the preset is used if the
 * variable is missing, and the fallback is used if the preset is
 * also missing.
 *
 * <p>Like {@link Login}, this value is not required to be non-empty:
 * an empty password passes through as is, so that contests which do
 * not require authentication can be configured without special-casing.
 *
 * <p>This class only carries the password. It does not mask, hash,
 * or log it. Callers that print configuration for debugging are
 * responsible for hiding the value themselves.
 */
public final class Password implements Text {
    private final Text origin;

    /**
     * Reads {@code PASSWORD} from {@code .env}.
     */
    public Password() {
        this(
            new ValueOf(
                new VarOfDotenv("PASSWORD")
            )
        );
    }

    /**
     * Reads {@code PASSWORD} from {@code .env}, using the given preset
     * and fallback if the variable is missing.
     *
     * @param d the preset to use when {@code PASSWORD} is missing
     * @param f the fallback to use when both are missing
     */
    public Password(final Text d, final Text f) {
        this(
            new ValueOf(
                new VarOfDotenv(
                    new Text.Of("PASSWORD"),
                    d, f
                )
            )
        );
    }

    /**
     * Uses the given text directly as the password, without reading
     * {@code .env} and without requiring the value to be non-empty.
     *
     * @param t the value to use as is
     */
    public Password(final Text t) {
        this.origin = new TextAbout(
            "user's ejudge contest password",
            t
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
