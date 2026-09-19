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

public final class AvailableProblems implements App {
    private final Effect src;

    public AvailableProblems() {
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

    public AvailableProblems(final Location l, final Credentials c, final Out o) {
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

    public AvailableProblems(final ContestResource c, final Out o) {
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

    public AvailableProblems(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
