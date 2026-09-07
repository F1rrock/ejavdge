package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

public final class Login implements Text {
    private final Text origin;

    public Login() {
        this(
            new ValueOf(
                new VarOfDotenv("LOGIN")
            )
        );
    }

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
