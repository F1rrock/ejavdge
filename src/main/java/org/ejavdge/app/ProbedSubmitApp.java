package org.ejavdge.app;

import org.ejavdge.app.scenario.SubmittingWithProbe;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class ProbedSubmitApp implements App {
    private final Effect src;

    public ProbedSubmitApp(final SubmittingWithProbe s, final LastReport r, final Out o) {
        this(
            new WritingOf(
                new Notice(s, r),
                o
            )
        );
    }

    public ProbedSubmitApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
