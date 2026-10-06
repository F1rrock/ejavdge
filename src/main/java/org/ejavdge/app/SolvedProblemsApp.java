package org.ejavdge.app;

import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.domain.problem.SolvedProbs;
import org.ejavdge.effect.Effect;
import org.ejavdge.scalar.text.BindOfText;
import org.ejavdge.scalar.text.Fallback;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

/**
 * Application entry point that displays the list of solved problems.
 * <p>
 * This class implements the {@link App} interface and delegates all work to an
 * {@link Effect} passed to its constructor. The chain of constructors allows
 * building the application from default settings, from a location and
 * credentials, or from a ready-made contest resource and output.
 */
public final class SolvedProblemsApp implements App {

    /**
     * The effect that is executed when the application runs.
     */
    private final Effect src;

    /**
     * Creates an application with default settings for location, credentials,
     * and output.
     */
    public SolvedProblemsApp() {
        this(
            new Location(
                new ClientPath(),
                new BaseUrl(),
                new Port()
            ),
            new Credentials(
                new Login(),
                new Password(),
                new ContestId()
            ),
            new PresetOut()
        );
    }

    /**
     * Creates an application for the given location, credentials, and output.
     * A contest resource and session are created internally using default
     * drivers.
     *
     * @param l the location (URL, path, port) of the contest system
     * @param c the credentials for authentication
     * @param o the output to which the result will be written
     */
    public SolvedProblemsApp(final Location l, final Credentials c, final Out o) {
        this(
            new ContestResource(
                new PresetDriver(),
                l,
                new Session(
                    new PresetDriver(),
                    l,
                    c
                )
            ),
            o
        );
    }

    /**
     * Creates an application that displays solved problems using the given
     * contest resource and output.
     * <p>
     * The list of solved problems is obtained from the main page of the
     * contest. If no problems have been solved, a fallback message is shown.
     *
     * @param c the contest resource providing access to the contest system
     * @param o the output to which the result will be written
     */
    public SolvedProblemsApp(final ContestResource c, final Out o) {
        this(
            new WritingOf(
                new BindOfText(
                    new SolvedProbs(
                        new PresetEngine(),
                        new MainPage(c)
                    ),
                    ps -> new Fallback(
                        new NonEmpty(new Text.Of(ps)),
                        new Text.Of("You have not solved anything yet :(")
                    )
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
    public SolvedProblemsApp(final Effect e) {
        this.src = e;
    }

    /**
     * Runs this application by executing its associated effect.
     */
    @Override
    public void run() {
        this.src.perform();
    }
}
