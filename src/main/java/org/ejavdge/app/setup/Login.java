package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

/**
 * The login of the account used to access the eJudge contest.
 *
 * <p>Read from the {@code LOGIN} variable in {@code .env}. When a
 * preset value and a fallback are given, the preset is used if the
 * variable is missing, and the fallback is used if the preset is
 * also missing.
 *
 * <p>Unlike {@link BaseUrl} and {@link ClientPath}, this value is not
 * required to be non-empty. An empty login is passed through as is,
 * so that anonymous or password-less contests can be configured
 * without special-casing.
 */
public final class Login implements Text {
    private final Text origin;

    /**
     * Reads {@code LOGIN} from {@code .env}.
     */
    public Login() {
        this(
            new ValueOf(
                new VarOfDotenv("LOGIN")
            )
        );
    }

    /**
     * Reads {@code LOGIN} from {@code .env}, using the given preset
     * and fallback if the variable is missing.
     *
     * @param d the preset to use when {@code LOGIN} is missing
     * @param f the fallback to use when both are missing
     */
    public Login(final Text d, final Text f) {
        this(
            new ValueOf(
                new VarOfDotenv(
                    new Text.Of("LOGIN"),
                    d, f
                )
            )
        );
    }

    /**
     * Uses the given text directly as the login, without reading
     * {@code .env} and without requiring the value to be non-empty.
     *
     * @param t the value to use as is
     */
    public Login(final Text t) {
        this.origin = new TextAbout(
            "ejudge contest login",
            t
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
