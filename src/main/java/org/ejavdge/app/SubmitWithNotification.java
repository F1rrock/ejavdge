package org.ejavdge.app;

import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.StatusInJson;
import org.ejavdge.domain.report.WithPolling;
import org.ejavdge.domain.report.ReportReadiness;
import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.effect.WithTimeout;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

import java.time.Duration;

public final class SubmitWithNotification implements App {
    private final Effect src;

    public SubmitWithNotification(final SilentSubmit s, final ContestResource r, final Out o) {
        this(
            new WritingOf(
                new Notice(
                    new Sequence(
                        new RunningOf(s),
                        new WithTimeout(
                            new WithPolling(
                                new WritingOf(
                                    new Text.Of("Tested!"),
                                    o
                                ),
                                new ReportReadiness(
                                    new StatusInJson(r)
                                ),
                                Duration.ofSeconds(1)
                            ),
                            Duration.ofSeconds(5)
                        )
                    ),
                    new Text.Of("Report is available!")
                ),
                o
            )
        );
    }

    public SubmitWithNotification(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
