package org.ejavdge.domain.report;

import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Map;
import org.ejavdge.items.SignificantLines;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

public final class ReportTable implements Text {
    private final Text origin;

    public ReportTable(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "table of problem's report",
                new Concat(
                    new Text.Of("\n"),
                    new Map<>(
                        l -> new Concat(new Text.Of("Result: "), l),
                        new SignificantLines(
                            new XmlSelection(
                                e, p,
                                new InnerText(
                                    new Text.Of("\n"),
                                    new OnlyAt(
                                        new Num.Of(2),
                                        new WithClass(
                                            "b1",
                                            new OnlyTag("td")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public ReportTable(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
