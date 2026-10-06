package org.ejavdge.app.scenario;

import org.ejavdge.app.setup.PresetDriver;
import org.ejavdge.auth.Session;
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
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;

import java.time.Duration;

/**
 * Submits a solution and waits until the judge has finished
 * checking it.
 *
 * <p>The submission itself is delegated to {@link
 * SubmittingOfSolution}. After the form is accepted, the effect
 * polls the contest server for the run's report until it is ready,
 * then returns. If the report does not become ready within the
 * timeout, the effect fails with {@link InvariantViolation} — it
 * does not return silently.
 *
 * <p>Use this scenario when the caller wants to know that the
 * submission has been judged, not just accepted. To submit and
 * return immediately, use {@link SubmittingOfSolution} directly.
 */
public final class SubmittingWithConfirmation implements Effect {
    private final Effect src;

    /**
     * @param f the solution file to submit
     * @param l the contest server to submit to
     * @param c the credentials to authenticate with
     */
    public SubmittingWithConfirmation(final ByteFile f, final Location l, final Credentials c) {
        this(
            f, l,
            new Session(
                new PresetDriver(),
                l, c
            )
        );
    }

    /**
     * @param f the solution file to submit
     * @param l the contest server to submit to
     * @param s the already authenticated session to reuse
     */
    public SubmittingWithConfirmation(final ByteFile f, final Location l, final Session s) {
        this(
            new SubmittingOfSolution(f, l, s),
            new ContestResource(
                new PresetDriver(),
                l, s
            ),
            Duration.ofSeconds(1)
        );
    }

    /**
     * @param s the submission to send before waiting for the report
     * @param r the contest resource to poll for the run's status
     * @param p the interval between two consecutive polls
     */
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

    /**
     * @param e the effect to delegate to
     */
    public SubmittingWithConfirmation(final Effect e) {
        this.src = e;
    }

    /**
     * Submits the solution, then polls the server until the report
     * is ready.
     *
     * @throws InvariantViolation if the submission fails, or if the
     *     report does not become ready within five seconds
     */
    @Override
    public void perform() throws InvariantViolation {
        this.src.perform();
    }
}
