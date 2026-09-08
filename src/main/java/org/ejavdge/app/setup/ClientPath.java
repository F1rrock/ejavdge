package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

public final class ClientPath implements Text {
    private final Text origin;

    public ClientPath() {
        this(
            new ValueOf(
                new VarOfDotenv("CLIENT_PATH")
            )
        );
    }

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
