package org.ejavdge.app;

import org.ejavdge.app.setup.PresetDriver;
import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.ProbAttachments;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.DownloadingOf;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ProbId;

public final class AttachmentsDownload implements App {
    private final Effect src;

    public AttachmentsDownload(final ContestResource r, final ByteFile f, final Text d) {
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

    public AttachmentsDownload(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
