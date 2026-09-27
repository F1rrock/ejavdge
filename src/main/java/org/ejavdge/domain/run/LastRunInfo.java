package org.ejavdge.domain.run;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.*;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.*;

public final class LastRunInfo implements Text {
    private final Text origin;

    public LastRunInfo(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "problem's last run info",
                new BindOfText(
                    p,
                    page -> new Concat(
                        new Text.Of("\n"),
                        new Map<>(
                            row -> new Concat(new Text.Of(": "), row),
                            new ZipOf<>(
                                new SignificantLines(
                                    new XmlSelection(
                                        e,
                                        new ProblemPage(new Text.Of(page)),
                                        new InnerText(
                                            new Text.Of("\n"),
                                            new OnlyAt(
                                                new Num.Of(1),
                                                new PathToTableRows()
                                            )
                                        )
                                    )
                                ),
                                new SignificantLines(
                                    new NonEmpty(
                                        new XmlSelection(
                                            e,
                                            new ProblemPage(new Text.Of(page)),
                                            new InnerText(
                                                new Text.Of("\n"),
                                                new OnlyAt(
                                                    new Num.Of(2),
                                                    new PathToTableRows()
                                                )
                                            )
                                        ),
                                        new Text.Of("There is no reports yet.")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public LastRunInfo(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
