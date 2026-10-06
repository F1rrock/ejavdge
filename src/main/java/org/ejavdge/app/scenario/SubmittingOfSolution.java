package org.ejavdge.app.scenario;

import org.ejavdge.app.setup.PresetDriver;
import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestForm;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.domain.solution.Solution;
import org.ejavdge.domain.solution.SolutionLang;
import org.ejavdge.effect.Effect;
import org.ejavdge.effect.SendingOf;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.context.*;

import java.util.function.Function;

/**
 * Submits a solution to a problem on the contest server.
 *
 * <p>The problem is identified by the name marker in the given
 * solution file (see {@link ProbNameOf}). The language is taken
 * from the problem page's preset, or from a language marker in the
 * file when the page offers a selector (see {@link SolutionLang}).
 * The submission is sent through the contest form.
 */
public final class SubmittingOfSolution implements Effect {
    private final Effect origin;

    /**
     * @param f the solution file to submit
     * @param l the contest server to submit to
     * @param c the credentials to authenticate with
     */
    public SubmittingOfSolution(final ByteFile f, final Location l, final Credentials c) {
        this(
            f, l,
            new Session(
                new PresetDriver(),
                l, c
            )
        );
    }

    /**
     * @param f the solution file to submit
     * @param l the contest server to submit to
     * @param s the already authenticated session to reuse
     */
    public SubmittingOfSolution(final ByteFile f, final Location l, final Session s) {
        this(
            f,
            new ContestForm(
                new PresetDriver(),
                l, s
            ),
            new ContestResource(
                new PresetDriver(),
                l, s
            )
        );
    }

    /**
     * @param f the solution file to submit
     * @param cf the contest form to send the submission through
     * @param cr the contest resource to fetch the problem page from
     */
    public SubmittingOfSolution(final ByteFile f, final ContestForm cf, final ContestResource cr) {
        this(
            new BindNumToEffect(
                new ProbByName(
                    new PresetEngine(),
                    new MainPage(cr),
                    new ProbNameOf(f)
                ),
                id -> new SendingOf(
                    new Solution(
                        cf,
                        new ContextOfSolution(
                            new ProbId(id),
                            new LangId(
                                new SolutionLang(
                                    new PresetEngine(),
                                    new ProblemPage(
                                        cr,
                                        new ProbId(id)
                                    ),
                                    f
                                )
                            )
                        ),
                        f
                    )
                )
            )
        );
    }

    /**
     * @param e the effect to delegate to
     */
    public SubmittingOfSolution(final Effect e) {
        this.origin = e;
    }

    /**
     * Resolves the problem and language, then sends the solution to
     * the contest form.
     *
     * @throws InvariantViolation if the problem marker is missing in
     *     the solution file, the problem page cannot be fetched, the
     *     language marker is missing when the page offers a selector,
     *     or the contest form rejects the submission
     */
    @Override
    public void perform() throws InvariantViolation {
        this.origin.perform();
    }

    /**
     * Binds a {@link Num} to an effect produced from its value.
     *
     * <p>The effect is only built and performed once {@link
     * Num#value()} returns, so a failure while resolving the number
     * propagates from {@link #perform()} rather than from the
     * constructor.
     */
    private static final class BindNumToEffect implements Effect {
        private final Num src;
        private final Function<Integer, Effect> func;

        public BindNumToEffect(final Num n, final Function<Integer, Effect> f) {
            this.src = n;
            this.func = f;
        }

        @Override
        public void perform() throws InvariantViolation {
            this.func.apply(this.src.value()).perform();
        }
    }
}
