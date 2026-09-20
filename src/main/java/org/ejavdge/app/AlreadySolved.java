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

public final class AlreadySolved implements App {
    private final Effect src;

    public AlreadySolved() {
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

    public AlreadySolved(final Location l, final Credentials c, final Out o) {
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

    public AlreadySolved(final ContestResource c, final Out o) {
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

    public AlreadySolved(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
