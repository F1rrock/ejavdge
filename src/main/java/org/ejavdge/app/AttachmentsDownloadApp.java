package org.ejavdge.app;

import org.ejavdge.app.scenario.DownloadingOfAttachments;
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

public final class AttachmentsDownloadApp implements App {
    private final Effect src;

    public AttachmentsDownloadApp(final ByteFile f, final Text d) {
        this(
            f, d,
            new Location(
                new BaseUrl(),
                new ClientPath(),
                new Port()
            )
        );
    }

    public AttachmentsDownloadApp(final ByteFile f, final Text d, final Location l) {
        this(
            new DownloadingOfAttachments(
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

    public AttachmentsDownloadApp(final DownloadingOfAttachments d, final Out o) {
        this(
            new WritingOf(
                new Notice(
                    d,
                    new Text.Of("Attachments successfully downloaded!")
                ),
                o
            )
        );
    }

    public AttachmentsDownloadApp(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
