package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.app.scenario.DownloadingOfAttachments;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class SilentSubmitAppTest extends TestCase {
    public void testNotice() {
        final var buffer = new StringBuilder();
        new AttachmentsDownloadApp(
            new DownloadingOfAttachments(() -> {}),
            text -> buffer.append(text.content())
        ).run();
        assertFalse(buffer.toString().isEmpty());
    }

    public void testTotalDownloads() {
        final var calls = new AtomicInteger(0);
        new AttachmentsDownloadApp(
            new DownloadingOfAttachments(calls::incrementAndGet),
            Text::content
        ).run();
        assertEquals(1, calls.get());
    }

    public void testBrokenAttachments() {
        try {
            new AttachmentsDownloadApp(
                new DownloadingOfAttachments(() -> {
                    throw new InvariantViolation("There is no attachments.");
                }),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
