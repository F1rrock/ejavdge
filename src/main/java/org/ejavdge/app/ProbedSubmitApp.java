package org.ejavdge.app;

import org.ejavdge.app.scenario.SubmittingWithConfirmation;
import org.ejavdge.app.scenario.SubmittingWithProbe;
import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class ProbedSubmitApp implements App {
    private final Effect src;

    public ProbedSubmitApp(final ByteFile f, final Text d) {
        this(
            new JavaProgram(f, d),
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
        );
    }

    public ProbedSubmitApp(final JavaProgram p, final Location l, final Credentials c) {
        this(
            p, l,
            new Session(
                new PresetDriver(),
                l, c
            )
        );
    }

    public ProbedSubmitApp(final JavaProgram p, final Location l, final Session s) {
        this(
            new SubmittingWithProbe(
                new VerdictOfProbe(
                    p,
                    new PresetEngine(),
                    new ContestResource(
                        new PresetDriver(),
                        l, s
                    )
                ),
                new SubmittingWithConfirmation(p, l, s)
            )
        );
    }

    public ProbedSubmitApp(final SubmittingWithProbe s, final LastReport r, final Out o) {
        this(
            new WritingOf(
                new Notice(s, r),
                o
            )
        );
    }

    public ProbedSubmitApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
