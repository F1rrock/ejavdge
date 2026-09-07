package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

public final class Password implements Text {
    private final Text origin;

    public Password() {
        this(
            new ValueOf(
                new VarOfDotenv("PASSWORD")
            )
        );
    }

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
