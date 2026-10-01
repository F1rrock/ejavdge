package org.ejavdge.app;

import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class LastReportApp implements App {
    private final Effect src;

    public LastReportApp(final ByteFile f, final ContestResource r, final Out o) {
        this(
            new LastReport(f, new PresetEngine(), r),
            o
        );
    }

    public LastReportApp(final LastReport r, final Out o) {
        this(new WritingOf(r, o));
    }

    public LastReportApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
