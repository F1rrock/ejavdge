package org.ejavdge.app.scenario;

import org.ejavdge.contest.ContestForm;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.StatusInJson;
import org.ejavdge.domain.report.ReportReadiness;
import org.ejavdge.domain.report.AwaitingOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.effect.WithTimeout;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;

import java.time.Duration;

public final class SubmittingWithConfirmation implements Effect {
    private final Effect src;

    public SubmittingWithConfirmation(final ByteFile f, final ContestForm cf, final ContestResource cr) {
        this(new SubmittingOfSolution(f, cf, cr), cr, Duration.ofSeconds(1));
    }

    public SubmittingWithConfirmation(final SubmittingOfSolution s, final ContestResource r, final Duration p) {
        this(
            new Sequence(
                s,
                new WithTimeout(
                    new AwaitingOf(
                        new ReportReadiness(
                            new StatusInJson(r)
                        ),
                        p
                    ),
                    Duration.ofSeconds(5)
                )
            )
        );
    }

    public SubmittingWithConfirmation(final Effect e) {
        this.src = e;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.src.perform();
    }
}
