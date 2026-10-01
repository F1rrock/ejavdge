package org.ejavdge.app.scenario;

import org.ejavdge.app.setup.PresetDriver;
import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.ProbAttachments;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.DownloadingOf;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ProbId;

public final class DownloadingOfAttachments implements Effect {
    private final Effect src;

    public DownloadingOfAttachments(final ContestResource r, final ByteFile f, final Text d) {
        this(
            new DownloadingOf(
                new ProbAttachments(
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
                new PresetDriver(),
                d
            )
        );
    }

    public DownloadingOfAttachments(final Effect e) {
        this.src = e;
    }

    @Override
    public void perform() throws InvariantViolation {
        this.src.perform();
    }
}
