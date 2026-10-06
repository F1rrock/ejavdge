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
import org.ejavdge.file.JavaProgram;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

/**
 * Application entry point that performs a probed submission.
 * <p>
 * This class implements the {@link App} interface and delegates all work to an
 * {@link Effect} passed to its constructor. The chain of constructors allows
 * building the application from ready-made components or creating them by
 * default from a given program, location, and credentials.
 */
public final class ProbedSubmitApp implements App {

    /**
     * The effect that is executed when the application runs.
     */
    private final Effect src;

    /**
     * Creates an application for the given Java program using default
     * settings for location and credentials.
     *
     * @param p the Java program to submit
     */
    public ProbedSubmitApp(final JavaProgram p) {
        this(
            p,
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

    /**
     * Creates an application for the given program, location, and credentials.
     * A new session is created using a default driver.
     *
     * @param p the Java program to submit
     * @param l the location (URL, path, port) of the contest system
     * @param c the credentials for authentication
     */
    public ProbedSubmitApp(final JavaProgram p, final Location l, final Credentials c) {
        this(
            p, l,
            new Session(
                new PresetDriver(),
                l, c
            )
        );
    }

    /**
     * Creates an application for the given program, location, and an existing
     * session. A report and an output for it are constructed internally.
     *
     * @param p the Java program to submit
     * @param l the location of the contest system
     * @param s the active session for interacting with the contest system
     */
    public ProbedSubmitApp(final JavaProgram p, final Location l, final Session s) {
        this(
            new Report(p, l, s),
            new PresetOut()
        );
    }

    /**
     * Creates an application from a ready-made report and an output method.
     *
     * @param r the report containing the results of probing and submission
     * @param o the output to which the report will be written
     */
    public ProbedSubmitApp(final Report r, final Out o) {
        this(new WritingOf(r, o));
    }

    /**
     * Creates an application that combines a submission scenario with probing,
     * the last report, and an output, wrapping this into a notice.
     *
     * @param s the submission scenario with preliminary probing
     * @param r the last report about the performed submission
     * @param o the output to which the notice will be sent
     */
    public ProbedSubmitApp(final SubmittingWithProbe s, final LastReport r, final Out o) {
        this(
            new WritingOf(
                new Notice(s, r),
                o
            )
        );
    }

    /**
     * Creates an application with the given effect.
     *
     * @param e the effect to be executed when the application runs
     */
    public ProbedSubmitApp(final Effect e) {
        this.src = e;
    }

    /**
     * Runs this application by executing its associated effect.
     *
     * @throws InvariantViolation if an invariant is violated during execution
     */
    @Override
    public void run() throws InvariantViolation {
        this.src.perform();
    }

    /**
     * A report produced as a result of the application's work.
     * <p>
     * It is essentially a textual representation that delegates content
     * retrieval to an enclosed {@link Text}. Depending on the constructor used,
     * the report can be built either as a notice about the result of probing
     * and submission or as a wrapper around arbitrary text.
     */
    public static final class Report implements Text {

        /**
         * The source of the report's textual content.
         */
        private final Text origin;

        /**
         * Creates a report for the given program, location, and credentials.
         * A session with a default driver is created internally.
         *
         * @param p the Java program for which the report is built
         * @param l the location of the contest system
         * @param c the credentials for authentication
         */
        public Report(final JavaProgram p, final Location l, final Credentials c) {
            this(
                p, l,
                new Session(
                    new PresetDriver(),
                    l, c
                )
            );
        }

        /**
         * Creates a report for the given program, location, and an existing
         * session. The report includes a notice about the probing result and
         * the last submission report.
         *
         * @param p the Java program for the report
         * @param l the location of the contest system
         * @param s the active session
         */
        public Report(final JavaProgram p, final Location l, final Session s) {
            this(
                new Notice(
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
                    ),
                    new LastReport(
                        p,
                        new PresetEngine(),
                        new ContestResource(
                            new PresetDriver(),
                            l, s
                        )
                    )
                )
            );
        }

        /**
         * Creates a report that wraps arbitrary text.
         *
         * @param t the text that will become the report's content
         */
        public Report(final Text t) {
            this.origin = t;
        }

        /**
         * Returns the textual content of the report.
         *
         * @return the content of the report
         * @throws InvariantViolation if an invariant is violated while retrieving the content
         */
        @Override
        public String content() throws InvariantViolation {
            return this.origin.content();
        }
    }
}
