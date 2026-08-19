package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.context.WithEntry;

public final class ProblemPage implements Text {
    private final Text origin;

    public ProblemPage(final ContestResource r, final ProbId p) {
        this(
            new TextAbout(
                "problem page",
                new Utf8Text(
                    new ContestResource(
                        r,
                        new WithEntry(
                            new Text.Of("action"),
                            new TextOfNum(139),
                            p
                        )
                    )
                )
            )
        );
    }

    public ProblemPage(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
