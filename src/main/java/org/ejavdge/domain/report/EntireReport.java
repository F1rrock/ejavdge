package org.ejavdge.domain.report;

import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.*;

public final class EntireReport implements Text {
    private final Text origin;

    public EntireReport(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "problem's report",
                new Trimmed(
                    new BindOfText(
                        p,
                        page -> new Concat(
                            new Text.Of("\n"),
                            new Items.Of<>(
                                new ReportHeader(
                                    e,
                                    new ReportPage(new Text.Of(page))
                                ),
                                new ReportTable(
                                    e,
                                    new ReportPage(new Text.Of(page))
                                ),
                                new ReportDetails(
                                    e,
                                    new ReportPage(new Text.Of(page))
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public EntireReport(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
