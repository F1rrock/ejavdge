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

/**
 * Downloads the attachments of a problem into a local directory.
 *
 * <p>The problem is identified by the name marker in the given
 * solution file (see {@link ProbNameOf}). The problem page is
 * fetched through the contest resource, attachment links are
 * extracted from it, and each file is saved under the given
 * directory.
 */
public final class DownloadingOfAttachments implements Effect {
    private final Effect src;

    /**
     * @param r the contest to fetch the problem page from
     * @param f the solution file whose marker identifies the problem
     * @param d the local directory to save attachments into
     */
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

    /**
     * @param e the effect to delegate to
     */
    public DownloadingOfAttachments(final Effect e) {
        this.src = e;
    }

    /**
     * Fetches the problem page, extracts attachment links, and saves
     * each attachment under the target directory.
     *
     * @throws InvariantViolation if the problem marker is missing in
     *     the solution file, the problem page cannot be fetched, or
     *     any attachment cannot be downloaded
     */
    @Override
    public void perform() throws InvariantViolation {
        this.src.perform();
    }
}
