package org.ejavdge.app.scenario;

import org.ejavdge.domain.report.AffirmingOf;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class SubmittingWithProbe implements Effect {
    private final Effect origin;

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

    public SubmittingWithProbe(final Effect e) {
        this.origin = e;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.origin.perform();
    }
}
