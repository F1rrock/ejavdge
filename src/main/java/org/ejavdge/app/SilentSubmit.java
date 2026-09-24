package org.ejavdge.app;

import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestForm;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.domain.solution.Solution;
import org.ejavdge.domain.solution.SolutionLang;
import org.ejavdge.effect.SendingOf;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.context.ContextOfSolution;
import org.ejavdge.web.context.LangId;
import org.ejavdge.web.context.ProbId;

public final class SilentSubmit implements App {
    private final ByteFile file;
    private final ContestForm form;
    private final ContestResource resource;

    public SilentSubmit(final ByteFile f, final ContestForm cf, final ContestResource cr) {
        this.file = f;
        this.form = cf;
        this.resource = cr;
    }

    @Override
    public void run() throws InvariantViolation {
        final var pid = new ProbId(
            new Num.Of(
                new ProbByName(
                    new PresetEngine(),
                    new MainPage(this.resource),
                    new ProbNameOf(this.file)
                ).value()
            )
        );
        new SendingOf(
            new Solution(
                this.form,
                new ContextOfSolution(
                    pid,
                    new LangId(
                        new SolutionLang(
                            new PresetEngine(),
                            new ProblemPage(
                                this.resource,
                                pid
                            ),
                            this.file
                        )
                    )
                ),
                this.file
            )
        ).perform();
    }
}
