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

/**
 * Application entry point that displays the description of a problem.
 * <p>
 * This class implements the {@link App} interface and delegates all work to an
 * {@link Effect} passed to its constructor. The chain of constructors allows
 * building the application from ready-made components or creating them by
 * default from a given problem file.
 */
public final class ProblemDescriptionApp implements App {

    /**
     * The effect that is executed when the application runs.
     */
    private final Effect src;

    /**
     * Creates an application for the given problem file using default
     * settings for location, credentials, and output.
     *
     * @param f the byte file from which the problem name is derived
     */
    public ProblemDescriptionApp(final ByteFile f) {
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

    /**
     * Creates an application that constructs a detailed problem description
     * using the given contest resource, problem file, and output.
     *
     * @param r the contest resource providing access to the contest system
     * @param f the byte file from which the problem name is derived
     * @param o the output to which the description will be written
     */
    public ProblemDescriptionApp(final ContestResource r, final ByteFile f, final Out o) {
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

    /**
     * Creates an application with the given effect.
     *
     * @param e the effect to be executed when the application runs
     */
    public ProblemDescriptionApp(final Effect e) {
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
