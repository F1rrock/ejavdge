package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.RunId;
import org.ejavdge.web.context.WithEntry;

public final class ReportPage implements Text {
    private final Text origin;

    public ReportPage(final ContestResource cr, final RunId id) {
        this(
            new TextAbout(
                "report page",
                new Utf8Text(
                    new ContestResource(
                        cr,
                        new WithEntry(
                            new Text.Of("action"),
                            new TextOfNum(37),
                            id
                        )
                    )
                )
            )
        );
    }

    public ReportPage(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
