package org.ejavdge.app;

import org.ejavdge.app.scenario.SubmittingOfSolution;
import org.ejavdge.app.setup.*;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class SilentSubmitApp implements App {
    private final Effect src;

    public SilentSubmitApp(final ByteFile f) {
        this(
            new SubmittingOfSolution(
                f,
                new Location(
                    new BaseUrl(),
                    new ClientPath(),
                    new Port()
                ),
                new Credentials(
                    new Login(),
                    new Password(),
                    new ContestId()
                )
            ),
            new PresetOut()
        );
    }

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
