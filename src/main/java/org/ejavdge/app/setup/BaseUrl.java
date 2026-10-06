package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

/**
 * The hostname or IP address of the eJudge server.
 *
 * <p>Read from the {@code BASE_URL} variable in {@code .env}. When a
 * preset value and a fallback are given, the preset is used if the
 * variable is missing, and the fallback is used if the preset is
 * also missing.
 *
 * <p>The value is the host only — no scheme, no port, no path. The
 * scheme is implied by {@link Port}, and the path by
 * {@link ClientPath}.
 */
public final class BaseUrl implements Text {
    private final Text origin;

    /**
     * Reads {@code BASE_URL} from {@code .env}.
     */
    public BaseUrl() {
        this(
            new ValueOf(
                new VarOfDotenv("BASE_URL")
            )
        );
    }

    /**
     * Reads {@code BASE_URL} from {@code .env}, using the given
     * preset and fallback if the variable is missing.
     *
     * @param d the preset to use when {@code BASE_URL} is missing
     * @param f the fallback to use when both are missing
     */
    public BaseUrl(final Text d, final Text f) {
        this(
            new ValueOf(
                new VarOfDotenv(
                    new Text.Of("BASE_URL"),
                    d, f
                )
            )
        );
    }

    /**
     * Uses the given text directly as the base URL, requiring it to be
     * non-empty.
     *
     * @param t the value to use as is
     */
    public BaseUrl(final Text t) {
        this.origin = new TextAbout(
            "ejudge base url",
            new NonEmpty(t)
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
