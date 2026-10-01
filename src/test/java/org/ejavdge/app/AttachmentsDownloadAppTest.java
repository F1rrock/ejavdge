package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.app.flow.DownloadingOfAttachments;

public final class AttachmentsDownloadTest extends TestCase {
    public void testA() {
        final var buffer = new StringBuilder();
        new AttachmentsDownloadApp(
            new DownloadingOfAttachments(() -> {}),
            text -> buffer.append(text.content())
        ).run();
        assertFalse(buffer.toString().isEmpty());
    }
}
