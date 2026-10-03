package org.ejavdge.app;

import org.ejavdge.app.scenario.SubmittingWithConfirmation;
import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class ReportedSubmitApp implements App {
    private final Effect src;

    public ReportedSubmitApp(final ByteFile f) {
        this(
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
        );
    }

    public ReportedSubmitApp(final ByteFile f, final Location l, final Credentials c) {
        this(
            f, l,
            new Session(
                new PresetDriver(),
                l, c
            )
        );
    }

    public ReportedSubmitApp(final ByteFile f, final Location l, final Session s) {
        this(
            new Report(f, l, s),
            new PresetOut()
        );
    }

    public ReportedSubmitApp(final Report r, final Out o) {
        this(new WritingOf(r, o));
    }

    public ReportedSubmitApp(final SubmittingWithConfirmation s, final LastReport r, final Out o) {
        this(
            new WritingOf(
                new Notice(s, r),
                o
            )
        );
    }

    public ReportedSubmitApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }

    public static final class Report implements Text {
        private final Text origin;

        public Report(final ByteFile f, final Location l, final Credentials c) {
            this(
                f, l,
                new Session(
                    new PresetDriver(),
                    l, c
                )
            );
        }

        public Report(final ByteFile f, final Location l, final Session s) {
            this(
                new Notice(
                    new SubmittingWithConfirmation(f, l, s),
                    new LastReport(
                        f,
                        new PresetEngine(),
                        new ContestResource(
                            new PresetDriver(),
                            l, s
                        )
                    )
                )
            );
        }

        public Report(final Text t) {
            this.origin = t;
        }

        @Override
        public String content() throws InvariantViolation {
            return this.origin.content();
        }
    }
}
