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


public final class SubmittingOfSolution implements Effect {
    private final Effect origin;

    public SubmittingOfSolution(final ByteFile f, final Location l, final Credentials c) {
        this(
            f, l,
            new Session(
                new PresetDriver(),
                l, c
            )
        );
    }

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

    public SubmittingOfSolution(final Effect e) {
        this.origin = e;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.origin.perform();
    }

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
