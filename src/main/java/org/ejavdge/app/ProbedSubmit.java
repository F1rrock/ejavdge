package org.ejavdge.app;

import org.ejavdge.domain.report.AffirmingOf;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.effect.Effect;
import org.ejavdge.effect.Sequence;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class ProbedSubmit implements App {
    private final Effect src;

    public ProbedSubmit(final VerdictOfProbe v, final ReportedSubmit s, final Out o) {
        this(
            new WritingOf(
                new Notice(
                    new Sequence(
                        new AffirmingOf(
                            v,
                            new Text.Of("Some local tests failed.")
                        ),
                        new WritingOf(new Text.Of("All local tests passed."), o),
                        new RunningOf(s)
                    ),
                    new Empty()
                ),
                o
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
