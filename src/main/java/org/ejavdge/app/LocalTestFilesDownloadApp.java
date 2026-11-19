package org.ejavdge.app;

import org.ejavdge.app.scenario.DownloadingOfLocalTestFiles;
import org.ejavdge.app.setup.*;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.effect.Effect;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

/**
 * Downloads a problem's Local Test files into a local directory and
 * prints a success message.
 *
 * <p>The problem is identified by the name marker in the given
 * solution file. The page is fetched from the contest server, the
 * Local Test file links are extracted, and each file is written under the
 * target directory. On success, the message
 * {@code "Local Test files successfully downloaded!"} is written to the
 * output channel.
 */
public final class LocalTestFilesDownloadApp implements App {
    private final Effect src;

    /**
     * Creates a download application using the given solution file and
     * target directory, with default connection settings.
     *
     * @param f the solution file whose marker identifies the problem
     * @param d the local directory where files are saved
     */
    public LocalTestFilesDownloadApp(final ByteFile f, final Text d) {
        this(
            f, d,
            new Location(
                new BaseUrl(),
                new ClientPath(),
                new Port()
            )
        );
    }

    /**
     * Creates a download application using the given solution file,
     * target directory, and contest location.
     *
     * @param f the solution file whose marker identifies the problem
     * @param d the local directory where files are saved
     * @param l the contest server to fetch the problem page from
     */
    public LocalTestFilesDownloadApp(final ByteFile f, final Text d, final Location l) {
        this(
            new DownloadingOfLocalTestFiles(
                new ContestResource(
                    new PresetDriver(),
                    l,
                    new Session(
                        new PresetDriver(),
                        l,
                        new Credentials(
                            new Login(),
                            new Password(),
                            new ContestId()
                        )
                    )
                ),
                f, d
            ),
            new PresetOut()
        );
    }

    /**
     * Creates a download application using the given scenario and
     * output channel.
     *
     * @param d the scenario that performs the actual download
     * @param o the output channel for the success message
     */
    public LocalTestFilesDownloadApp(final DownloadingOfLocalTestFiles d, final Out o) {
        this(
            new WritingOf(
                new Notice(
                    d,
                    new Text.Of("Local Test files successfully downloaded!")
                ),
                o
            )
        );
    }

    /**
     * Creates a download application that delegates to the given
     * effect.
     *
     * @param e the effect to delegate to
     */
    public LocalTestFilesDownloadApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
