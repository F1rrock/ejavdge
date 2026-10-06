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

/**
 * Prints the report of the latest run of a problem.
 *
 * <p>The problem is identified by the name marker in the given
 * solution file. The report is fetched from the contest server (see
 * {@link LastReport}); it is written to the output channel as is,
 * without formatting or summarising.
 *
 * <p>Unlike {@code AttachmentsDownloadApp} and {@code
 * AvailableProblemsApp}, this app does not wrap failures in a
 * success message. It writes the fetched report directly to the
 * output channel and lets any failure propagate to the caller.
 */
public final class LastReportApp implements App {
    private final Effect src;

    /**
     * @param f the solution file whose marker identifies the problem
     */
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

    /**
     * @param f the solution file whose marker identifies the problem
     * @param l the contest server to fetch the report from
     */
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

    /**
     * @param f the solution file whose marker identifies the problem
     * @param r the contest resource to fetch the report from
     * @param o the output channel for the report
     */
    public LastReportApp(final ByteFile f, final ContestResource r, final Out o) {
        this(
            new LastReport(f, new PresetEngine(), r),
            o
        );
    }

    /**
     * @param r the report to print
     * @param o the output channel for the report
     */
    public LastReportApp(final LastReport r, final Out o) {
        this(new WritingOf(r, o));
    }

    /**
     * @param e the effect to delegate to
     */
    public LastReportApp(final Effect e) {
        this.src = e;
    }

    /**
     * Fetches the report of the problem's latest run and writes it to
     * the output channel.
     *
     * @throws InvariantViolation if the problem marker is missing in
     *     the solution file, the problem page or the report cannot be
     *     fetched, or the output channel cannot be written to
     */
    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }
}
