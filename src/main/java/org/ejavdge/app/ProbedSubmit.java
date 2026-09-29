package org.ejavdge.app;

import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.error.InvariantViolation;

public final class ProbedSubmit implements App {
    private final Effect src;

    public ProbedSubmit(final LocalProbe p, final ReportedSubmit s) {
        this(
            new Sequence(
                new RunningOf(p),
                new RunningOf(s)
            )
        );
    }

    public ProbedSubmit(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
