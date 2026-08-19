package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.WithEntry;

public final class RunStatus implements Text {
    private final Text origin;

    public RunStatus(final ContestResource r) {
        this(
            new TextAbout(
                "run status",
                new Utf8Text(
                    new ContestResource(
                        r,
                        new WithEntry(
                            new Text.Of("action"),
                            new TextOfNum(175),
                            new WithEntry(
                                new Text.Of("x"),
                                new TextOfNum(1)
                            )
                        )
                    )
                )
            )
        );
    }

    public RunStatus(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
