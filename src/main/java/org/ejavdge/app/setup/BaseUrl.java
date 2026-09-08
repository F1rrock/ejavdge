package org.ejavdge.app.setup;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.workspace.env.ValueOf;
import org.ejavdge.workspace.env.VarOfDotenv;

public final class BaseUrl implements Text {
    private final Text origin;

    public BaseUrl() {
        this(
            new ValueOf(
                new VarOfDotenv("BASE_URL")
            )
        );
    }

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
