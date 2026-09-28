package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.web.media.Media;

public final class RunId implements Context {
    private final Text src;

    public RunId(final String s) {
        this(new Text.Of(s));
    }

    public RunId(final Text t) {
        this.src = new TextAbout(
            "run id",
            new NonEmpty(
                t,
                new Text.Of("there is no run id")
            )
        );
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("run_id"), this.src)
            .content();
    }
}
