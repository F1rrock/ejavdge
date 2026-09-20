package org.ejavdge.app;

import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.ProbBrief;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.problem.ProbRefs;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.file.ByteFile;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.*;
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
                new BindOfText(
                    new ProblemPage(
                        r,
                        new ProbId(
                            new ProbByName(
                                new PresetEngine(),
                                new MainPage(r),
                                new ProbNameOf(f)
                            )
                        )
                    ),
                    page -> new Concat(
                        new Text.Of("\n"),
                        new Items.Of<>(
                            new ProbBrief(
                                new PresetEngine(),
                                new ProblemPage(new Text.Of(page))
                            ),
                            new BindOfText(
                                new ProbRefs(
                                    new PresetEngine(),
                                    new ProblemPage(new Text.Of(page))
                                ),
                                refs -> new Fallback(
                                    new Concat(
                                        new Text.Of("\n"),
                                        new Items.Of<>(
                                            new Text.Of("Used references:"),
                                            new NonEmpty(new Text.Of(refs))
                                        )
                                    ),
                                    new Empty()
                                )
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
