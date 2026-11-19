package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.app.scenario.DownloadingOfLocalTestFiles;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class LocalTestFilesDownloadAppTest extends TestCase {
    public void testNotice() {
        final var buffer = new StringBuilder();
        new LocalTestFilesDownloadApp(
            new DownloadingOfLocalTestFiles(() -> {}),
            text -> buffer.append(text.content())
        ).run();
        assertEquals("Local Test files successfully downloaded!", buffer.toString());
    }

    public void testTotalDownloads() {
        final var calls = new AtomicInteger(0);
        new LocalTestFilesDownloadApp(
            new DownloadingOfLocalTestFiles(calls::incrementAndGet),
            Text::content
        ).run();
        assertEquals(1, calls.get());
    }

    public void testFailedDownload() {
        try {
            new LocalTestFilesDownloadApp(
                new DownloadingOfLocalTestFiles(() -> {
                    throw new InvariantViolation("Local Test files could not be downloaded.");
                }),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
