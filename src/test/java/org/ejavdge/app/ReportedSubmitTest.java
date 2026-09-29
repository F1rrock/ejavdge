package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ReportedSubmitTest extends TestCase {
    public void testRunsEffectsInOrder() throws InvariantViolation {
        final var calls = new StringBuilder();
        new ReportedSubmit(
            new SubmitWithNotification(
                () -> calls.append("Submit")
            ),
            new LastReport(
                () -> calls.append("Report")
            )
        ).run();
        assertEquals("SubmitReport", calls.toString());
    }

    public void testPropagatesSubmitFailure() {
        final var report = new AtomicBoolean(false);
        try {
            new ReportedSubmit(
                new SubmitWithNotification(
                    () -> {
                        throw new InvariantViolation("Submission failed");
                    }
                ),
                new LastReport(
                    () -> report.set(true)
                )
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertFalse(report.get());
        }
    }

    public void testPropagatesReportFailure() {
        final var submit = new AtomicBoolean(false);
        try {
            new ReportedSubmit(
                new SubmitWithNotification(
                    () -> submit.set(true)
                ),
                new LastReport(
                    () -> {
                        throw new InvariantViolation("Report failed");
                    }
                )
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation err) {
            assertTrue(submit.get());
        }
    }
}
