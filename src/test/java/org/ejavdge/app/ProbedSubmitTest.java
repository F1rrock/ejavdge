package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ProbedSubmitTest extends TestCase {
    public void testOrderOfEffects() throws InvariantViolation {
        final var calls = new StringBuilder();
        new ProbedSubmit(
            new LocalProbe(
                () -> calls.append("Probe")
            ),
            new ReportedSubmit(
                () -> calls.append("Submit")
            )
        ).run();
        assertEquals("ProbeSubmit", calls.toString());
    }

    public void testBrokenProbe() {
        final var submit = new AtomicBoolean(false);
        try {
            new ProbedSubmit(
                new LocalProbe(
                    () -> {
                        throw new InvariantViolation("Can not probe a solution.");
                    }
                ),
                new ReportedSubmit(
                    () -> submit.set(true)
                )
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertFalse(submit.get());
        }
    }

    public void testBrokenSubmit() {
        final var probed = new AtomicBoolean(false);
        try {
            new ProbedSubmit(
                new LocalProbe(
                    () -> probed.set(true)
                ),
                new ReportedSubmit(
                    () -> {
                        throw new InvariantViolation("Report failed");
                    }
                )
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation err) {
            assertTrue(probed.get());
        }
    }
}
