package org.ejavdge.app;

import org.ejavdge.app.scenario.SubmittingOfSolution;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class SilentSubmitApp implements App {
    private final Effect src;

    public SilentSubmitApp(final SubmittingOfSolution s, final Out o) {
        this(
            new WritingOf(
                new Notice(
                    s,
                    new Text.Of("Sent!")
                ),
                o
            )
        );
    }

    public SilentSubmitApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
