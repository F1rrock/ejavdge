package org.ejavdge.app.scenario;

import org.ejavdge.domain.report.AffirmingOf;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * Submits a solution only after it has passed the local probe.
 *
 * <p>The probe verdict is checked first. If it does not hold, the
 * effect fails immediately with {@code "Some local tests failed."}
 * and nothing is sent to the contest server. If the probe passes,
 * the submission and its confirmation are delegated to {@link
 * SubmittingWithConfirmation}.
 *
 * <p>Use this scenario when the caller wants to avoid spending a
 * submission on a solution that already fails against the problem's
 * samples.
 */
public final class SubmittingWithProbe implements Effect {
    private final Effect origin;

    /**
     * @param v the probe verdict to affirm before submitting
     * @param s the submission to run once the probe has passed
     */
    public SubmittingWithProbe(final VerdictOfProbe v, final SubmittingWithConfirmation s) {
        this(
            new Sequence(
                new AffirmingOf(
                    v,
                    new Text.Of("Some local tests failed.")
                ),
                s
            )
        );
    }

    /**
     * @param e the effect to delegate to
     */
    public SubmittingWithProbe(final Effect e) {
        this.origin = e;
    }

    /**
     * Checks the probe verdict and, if it holds, submits the
     * solution and waits for the report.
     *
     * @throws InvariantViolation if the probe fails, the probe
     *     verdict cannot be resolved, or the submission fails
     */
    @Override
    public void perform() throws InvariantViolation {
        this.origin.perform();
    }
}
