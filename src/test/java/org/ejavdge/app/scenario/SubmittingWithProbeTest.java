package org.ejavdge.app.scenario;

import junit.framework.TestCase;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicInteger;

public final class SubmittingWithProbeTest extends TestCase {
    public void testSubmitCallsWithPassedProbe() {
        final var calls = new AtomicInteger(0);
        new SubmittingWithProbe(
            new VerdictOfProbe(() -> true),
            new SubmittingWithConfirmation(calls::incrementAndGet)
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testSubmitCallsWithFailedProbe() {
        final var calls = new AtomicInteger(0);
        try {
            new SubmittingWithProbe(
                new VerdictOfProbe(() -> false),
                new SubmittingWithConfirmation(calls::incrementAndGet)
            ).perform();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(0, calls.get());
        }
    }

    public void testBrokenProbe() {
        final var calls = new AtomicInteger(0);
        try {
            new SubmittingWithProbe(
                new VerdictOfProbe(
                    () -> {
                        throw new InvariantViolation("There is no verdict.");
                    }
                ),
                new SubmittingWithConfirmation(calls::incrementAndGet)
            ).perform();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(0, calls.get());
        }
    }

    public void testBrokenSubmit() {
        try {
            new SubmittingWithProbe(
                new VerdictOfProbe(() -> true),
                new SubmittingWithConfirmation(() -> {
                    throw new InvariantViolation("Can not submit.");
                })
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
