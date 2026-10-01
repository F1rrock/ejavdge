package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.app.scenario.DownloadingOfAttachments;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class LastReportAppTest extends TestCase {
    public void testNotice() {
        final var buffer = new StringBuilder();
        new LastReportApp(
            new LastReport(new Text.Of("contents of report")),
            text -> buffer.append(text.content())
        ).run();
        assertEquals("contents of report", buffer.toString());
    }

    public void testReportCalls() {
        final var calls = new AtomicInteger(0);
        new LastReportApp(
            new LastReport(() -> {
                calls.incrementAndGet();
                return "contents of report";
            }),
            Text::content
        ).run();
        assertEquals(1, calls.get());
    }

    public void testBrokenReport() {
        try {
            new LastReportApp(
                new LastReport(() -> {
                    throw new InvariantViolation("There is no report.");
                }),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
