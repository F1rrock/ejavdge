package org.ejavdge.app;

import org.ejavdge.app.flow.DownloadingOfAttachments;
import org.ejavdge.effect.Effect;
import org.ejavdge.scalar.text.Notice;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.workspace.out.Out;
import org.ejavdge.workspace.out.WritingOf;

public final class AttachmentsDownload implements App {
    private final Effect src;

    public AttachmentsDownload(final DownloadingOfAttachments d, final Out o) {
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

    public AttachmentsDownload(final Effect e) {
        this.src = e;
    }

    @Override
    public void run() {
        this.src.perform();
    }
}
