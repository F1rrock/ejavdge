package org.ejavdge.app;

import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.domain.report.Feedback;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.effect.Effect;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.palette.Green;
import org.ejavdge.scalar.text.palette.Red;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class LocalProbe implements App {
    private final Effect src;

    public LocalProbe(final JavaProgram p) {
        this(
            p,
            new ContestResource(
                new PresetDriver(),
                new Location(
                    new ClientPath(),
                    new BaseUrl(),
                    new Port()
                ),
                new Session(
                    new PresetDriver(),
                    new Location(
                        new ClientPath(),
                        new BaseUrl(),
                        new Port()
                    ),
                    new Credentials(
                        new Login(),
                        new Password(),
                        new ContestId()
                    )
                )
            ),
            new PresetOut()
        );
    }

    public LocalProbe(final JavaProgram p, final ContestResource r, final Out o) {
        this(new VerdictOfProbe(p, new PresetEngine(), r), o);
    }

    public LocalProbe(final VerdictOfProbe v, final Out o) {
        this(
            new WritingOf(
                new Feedback(
                    new Green(new Text.Of("Success: all local tests passed")),
                    new Red(new Text.Of("Fail: some local tests failed")),
                    v
                ),
                o
            )
        );
    }

    public LocalProbe(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
