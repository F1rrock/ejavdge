package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

/**
 * The path of the eJudge client endpoint on the server.
 *
 * <p>Read from the {@code CLIENT_PATH} variable in {@code .env}.
 * When a preset value and a fallback are given, the preset is used
 * if the variable is missing, and the fallback is used if the preset
 * is also missing.
 *
 * <p>The value is the path only — no scheme, no host, no port.
 * Together with {@link BaseUrl}, {@link Port}, and the request
 * location, it forms the full URL of the endpoint.
 */
public final class ClientPath implements Text {
    private final Text origin;

    /**
     * Reads {@code CLIENT_PATH} from {@code .env}.
     */
    public ClientPath() {
        this(
            new ValueOf(
                new VarOfDotenv("CLIENT_PATH")
            )
        );
    }

    /**
     * @param d the preset to use when {@code CLIENT_PATH} is missing
     * @param f the fallback to use when both are missing
     */
    public ClientPath(final Text d, final Text f) {
        this(
            new ValueOf(
                new VarOfDotenv(
                    new Text.Of("CLIENT_PATH"),
                    d, f
                )
            )
        );
    }

    /**
     * @param t the value to use as is
     */
    public ClientPath(final Text t) {
        this.origin = new TextAbout(
            "ejudge client path",
            new NonEmpty(t)
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
