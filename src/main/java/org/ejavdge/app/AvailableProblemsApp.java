package org.ejavdge.app;

import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.domain.problem.ProbCatalog;
import org.ejavdge.effect.Effect;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

/**
 * Lists the problems available in the current contest and prints
 * them.
 *
 * <p>The main page of the contest is fetched from the server, and
 * the problem names are extracted from its top navigation list (see
 * {@link ProbCatalog}). The result is written to the output channel,
 * one name per line.
 *
 * <p>If the underlying fetch or extraction fails, the error message
 * is written to the same output channel rather than propagated to
 * the caller.
 */
public final class AvailableProblemsApp implements App {
    private final Effect src;

    /**
     * Creates an application that lists available problems, using the
     * connection settings from {@code .env} and standard output.
     */
    public AvailableProblemsApp() {
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
     * Creates an application that lists available problems, using the
     * given location, credentials, and output channel.
     *
     * @param l the contest server to fetch the main page from
     * @param c the credentials to authenticate with
     * @param o the output channel for the problem list and any error
     *     report
     */
    public AvailableProblemsApp(final Location l, final Credentials c, final Out o) {
        this(
            new ContestResource(
                new PresetDriver(),
                l,
                new Session(
                    new PresetDriver(),
                    l, c
                )
            ),
            o
        );
    }

    /**
     * Creates an application that lists available problems, using the
     * given contest resource and output channel.
     *
     * @param c the contest resource to fetch the main page from
     * @param o the output channel for the problem list and any error
     *     report
     */
    public AvailableProblemsApp(final ContestResource c, final Out o) {
        this(
            new WritingOf(
                new ProbCatalog(
                    new PresetEngine(),
                    new MainPage(c)
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
    public AvailableProblemsApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
