package org.ejavdge.domain.run;

import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.Verdict;
import org.ejavdge.domain.problem.ProbBrief;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.problem.ProbExamples;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.domain.solution.VerdictBySamples;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.web.context.ProbId;

public final class VerdictOfProbe implements Verdict {
    private final Verdict origin;

    public VerdictOfProbe(final JavaProgram p, final ContestResource r) {
        this(
            new VerdictBySamples(
                p,
                new ProbExamples(
                    new ProbBrief(
                        new PresetEngine(),
                        new ProblemPage(
                            r,
                            new ProbId(
                                new ProbByName(
                                    new PresetEngine(),
                                    new MainPage(r),
                                    new ProbNameOf(p)
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public VerdictOfProbe(final Verdict v) {
        this.origin = v;
    }

    @Override
    public boolean ok() throws InvariantViolation {
        return this.origin.ok();
    }
}
