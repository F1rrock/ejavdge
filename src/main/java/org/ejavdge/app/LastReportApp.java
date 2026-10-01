package org.ejavdge.app;

import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class LastReportApp implements App {
    private final Effect src;

    public LastReportApp(final ByteFile f) {
        this(
            f,
            new Location(
                new BaseUrl(),
                new ClientPath(),
                new Port()
            )
        );
    }

    public LastReportApp(final ByteFile f, final Location l) {
        this(
            f,
            new ContestResource(
                new PresetDriver(),
                l,
                new Session(
                    new PresetDriver(),
                    l,
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

    public LastReportApp(final ByteFile f, final ContestResource r, final Out o) {
        this(
            new LastReport(f, new PresetEngine(), r),
            o
        );
    }

    public LastReportApp(final LastReport r, final Out o) {
        this(new WritingOf(r, o));
    }

    public LastReportApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
