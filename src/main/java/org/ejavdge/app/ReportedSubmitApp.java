package org.ejavdge.app;

import org.ejavdge.app.scenario.SubmittingWithConfirmation;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class ReportedSubmitApp implements App {
    private final Effect src;

    public ReportedSubmitApp(final SubmittingWithConfirmation s, final LastReport r, final Out o) {
        this(
            new WritingOf(
                new Notice(s, r),
                o
            )
        );
    }

    public ReportedSubmitApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
