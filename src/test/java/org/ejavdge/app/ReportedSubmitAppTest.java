package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.app.scenario.SubmittingWithConfirmation;
import org.ejavdge.domain.report.LastReport;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ReportedSubmitAppTest extends TestCase {
    public void testRunsEffectsInOrder() throws InvariantViolation {
        final var calls = new StringBuilder();
        new ReportedSubmitApp(
            new SubmittingWithConfirmation(
                () -> calls.append("Submit")
            ),
            new LastReport(
                new Text.Of("Report")
            ),
            text -> calls.append(text.content())
        ).run();
        assertEquals("SubmitReport", calls.toString());
    }

    public void testPropagatesSubmitFailure() {
        final var report = new AtomicBoolean(false);
        try {
            new ReportedSubmitApp(
                new SubmittingWithConfirmation(
                    () -> {
                        throw new InvariantViolation("Submission failed");
                    }
                ),
                new LastReport(
                    () -> {
                        report.set(true);
                        return "report";
                    }
                ),
                Text::content
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertFalse(report.get());
        }
    }

    public void testPropagatesReportFailure() {
        final var submit = new AtomicBoolean(false);
        try {
            new ReportedSubmitApp(
                new SubmittingWithConfirmation(
                    () -> submit.set(true)
                ),
                new LastReport(
                    () -> {
                        throw new InvariantViolation("Report failed");
                    }
                ),
                Text::content
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation err) {
            assertTrue(submit.get());
        }
    }
}
