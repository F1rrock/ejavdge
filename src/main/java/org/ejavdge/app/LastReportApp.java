package org.ejavdge.app;

import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.contest.ReportPage;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.report.EntireReport;
import org.ejavdge.domain.run.LastRunId;
import org.ejavdge.domain.run.LastRunInfo;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.context.RunId;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class LastReportApp implements App {
    private final Effect src;

    public LastReportApp(final ByteFile f, final ContestResource r, final Out o) {
        this(
            new WritingOf(
                new BindOfText(
                    new ProblemPage(
                        r,
                        new ProbId(
                            new ProbByName(
                                new PresetEngine(),
                                new MainPage(r),
                                new ProbNameOf(f)
                            )
                        )
                    ),
                    page -> new BindOfText(
                        new EntireReport(
                            new PresetEngine(),
                            new ReportPage(
                                r,
                                new RunId(
                                    new LastRunId(
                                        new PresetEngine(),
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
                                    new PresetEngine(),
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
                ),
                o
            )
        );
    }

    public LastReportApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
