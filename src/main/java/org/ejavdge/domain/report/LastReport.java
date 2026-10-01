package org.ejavdge.domain.report;

import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.run.LastRunId;
import org.ejavdge.domain.run.LastRunInfo;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.context.RunId;

public final class LastReport implements Text {
    private final Text origin;

    public LastReport(final ByteFile f, final XmlEngine e, final ContestResource r) {
        this(
            new BindOfText(
                new ProblemPage(
                    r,
                    new ProbId(
                        new ProbByName(
                            e,
                            new MainPage(r),
                            new ProbNameOf(f)
                        )
                    )
                ),
                page -> new BindOfText(
                    new EntireReport(
                        e,
                        new ReportPage(
                            r,
                            new RunId(
                                new LastRunId(
                                    e,
                                    new ProblemPage(
                                        new Text.Of(page)
                                    )
                                )
                            )
                        )
                    ),
                    report -> new Fallback(
                        new NonEmpty(new Text.Of(report)),
                        new NonEmpty(
                            new LastRunInfo(
                                e,
                                new ProblemPage(
                                    new Text.Of(page)
                                )
                            ),
                            new Concat(
                                "There is no available info about ",
                                "last run of current problem"
                            )
                        )
                    )
                )
            )
        );
    }

    public LastReport(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
