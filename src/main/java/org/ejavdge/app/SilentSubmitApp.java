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

/**
 * Application entry point that performs a silent submission.
 * <p>
 * This class implements the {@link App} interface and delegates all work to an
 * {@link Effect} passed to its constructor. The chain of constructors allows
 * building the application from a solution file using default settings, or
 * from a ready-made submission scenario and output.
 */
public final class SilentSubmitApp implements App {

    /**
     * The effect that is executed when the application runs.
     */
    private final Effect src;

    /**
     * Creates an application for the given solution file using default
     * settings for location, credentials, and output.
     * <p>
     * The submission is performed via {@link SubmittingOfSolution}, and the
     * result is wrapped into a simple "Sent!" notice.
     *
     * @param f the byte file containing the solution to submit
     */
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

    /**
     * Creates an application from a submission scenario and an output,
     * wrapping the result into a notice.
     *
     * @param s the submission scenario to execute
     * @param o the output to which the notice will be written
     */
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

    /**
     * Creates an application with the given effect.
     *
     * @param e the effect to be executed when the application runs
     */
    public SilentSubmitApp(final Effect e) {
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
}
