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

/**
 * Runs a solution against the samples of a problem and prints a
 * pass/fail verdict.
 *
 * <p>The problem is identified by the name marker in the solution
 * file. The samples are extracted from the problem page and executed
 * against the program (see {@link VerdictOfProbe}). On success, the
 * message {@code "Success: all local tests passed"} is printed in
 * green; on failure, {@code "Fail: some local tests failed"} in red.
 *
 * <p>Nothing is submitted to the server. This app only runs the
 * solution locally, so it is safe to call repeatedly while iterating
 * on a solution.
 *
 * <p>If the underlying fetch or execution fails, the error message is
 * written to the output channel rather than propagated to the caller.
 */
public final class LocalProbeApp implements App {
    private final Effect src;

    /**
     * Creates an application that probes the given solution against the
     * samples of the problem identified by the solution file's marker,
     * using the default connection settings from {@code .env} and
     * standard output.
     *
     * @param p the solution to run against the problem's samples
     */
    public LocalProbeApp(final JavaProgram p) {
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

    /**
     * Creates an application that probes the given solution against the
     * samples of the problem identified by the solution file's marker,
     * using the supplied contest resource and output channel.
     *
     * @param p the solution to run against the problem's samples
     * @param r the contest resource to fetch the problem page from
     * @param o the output channel for the verdict and any error
     *     report
     */
    public LocalProbeApp(final JavaProgram p, final ContestResource r, final Out o) {
        this(new VerdictOfProbe(p, new PresetEngine(), r), o);
    }

    /**
     * Creates an application that renders the given probe verdict as a
     * success or failure message on the given output channel.
     * <p>
     * A successful verdict produces a green
     * {@code "Success: all local tests passed"} message; a failed
     * verdict produces a red {@code "Fail: some local tests failed"}
     * message.
     *
     * @param v the verdict to render as a success or failure message
     * @param o the output channel for the verdict and any error
     *     report
     */
    public LocalProbeApp(final VerdictOfProbe v, final Out o) {
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

    /**
     * Creates an application that delegates to the given effect.
     *
     * @param e the effect to delegate to
     */
    public LocalProbeApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
