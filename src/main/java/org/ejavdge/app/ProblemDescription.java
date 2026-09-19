package org.ejavdge.app;

import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.ProbBrief;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.file.ByteFile;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class ProblemDescription implements App {
    private final Effect src;

    public ProblemDescription(final ByteFile f) {
        this(
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
            f,
            new PresetOut()
        );
    }

    public ProblemDescription(final ContestResource r, final ByteFile f, final Out o) {
        this(
            new WritingOf(
                new ProbBrief(
                    new PresetEngine(),
                    new ProblemPage(
                        r,
                        new ProbId(
                            new ProbByName(
                                new PresetEngine(),
                                new MainPage(r),
                                new ProbNameOf(f)
                            )
                        )
                    )
                ),
                o
            )
        );
    }

    public ProblemDescription(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
