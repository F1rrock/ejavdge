package org.ejavdge.domain.report;

import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

public final class ReportHeader implements Text {
    private final Text origin;

    public ReportHeader(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "header of problem's report",
                new XmlSelection(
                    e, p,
                    new InnerText(
                        new BeforeTag(
                            "table",
                            new WithoutTags(
                                new Items.Of<>(
                                    new Text.Of("br"),
                                    new Text.Of("p")
                                ),
                                new ChildrenOf(
                                    new WithClass(
                                        "l14",
                                        new OnlyTag("div")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public ReportHeader(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
