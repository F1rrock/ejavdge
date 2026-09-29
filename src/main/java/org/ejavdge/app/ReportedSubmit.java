package org.ejavdge.app;

import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.error.InvariantViolation;

public final class ReportedSubmit implements App {
    private final Effect src;

    public ReportedSubmit(final SubmitWithNotification s, final LastReport r) {
        this(
            new Sequence(
                new RunningOf(s),
                new RunningOf(r)
            )
        );
    }

    public ReportedSubmit(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
