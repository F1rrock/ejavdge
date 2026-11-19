package org.ejavdge.app.scenario;

import org.ejavdge.app.setup.PresetDriver;
import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.domain.problem.LocalTestFiles;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.DownloadingOf;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ProbId;

/**
 * Downloads a problem's Local Test files into a local directory.
 *
 * <p>The problem is identified by the name marker in the given
 * solution file (see {@link ProbNameOf}). The problem page is
 * fetched through the contest resource, Local Test file links are extracted,
 * and each file is saved in the target directory.
 */
public final class DownloadingOfLocalTestFiles implements Effect {
    private final Effect src;

    /**
     * Creates a download scenario that fetches the problem page
     * identified by the given solution file and saves its Local Test
     * files in the target directory.
     *
     * @param r the contest to fetch the problem page from
     * @param f the solution file whose marker identifies the problem
     * @param d the local directory where files are saved
     */
    public DownloadingOfLocalTestFiles(final ContestResource r, final ByteFile f, final Text d) {
        this(
            new DownloadingOf(
                new LocalTestFiles(
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
     * Creates a download scenario that delegates to the given effect.
     *
     * @param e the effect to delegate to
     */
    public DownloadingOfLocalTestFiles(final Effect e) {
        this.src = e;
    }

    /**
     * Fetches the problem page, extracts Local Test file links, and
     * saves the files in the target directory.
     *
     * @throws InvariantViolation if the problem marker is missing in
     *     the solution file, the problem page cannot be fetched, or
     *     any Local Test file cannot be downloaded
     */
    @Override
    public void perform() throws InvariantViolation {
        this.src.perform();
    }
}
