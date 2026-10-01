package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class AwaitingOfTest extends TestCase {
    public void testWithAlwaysPositiveVerdict() {
        final var calls = new AtomicInteger(0);
        new AwaitingOf(
            () -> {
                calls.incrementAndGet();
                return true;
            },
            Duration.ZERO
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testRepeatingUntilVerdictIsOk() {
        final var attempts = new AtomicInteger(0);
        new AwaitingOf(
            () -> attempts.incrementAndGet() >= 10,
            Duration.ZERO
        ).perform();
        assertEquals(10, attempts.get());
    }

    public void testBrokenVerdict() {
        try {
            new AwaitingOf(
                () -> {
                    throw new InvariantViolation(
                        "There is no info about verdict"
                    );
                },
                Duration.ZERO
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testDelaysBetweenAttempts() {
        final var attempts = new AtomicInteger(0);
        final var start = System.nanoTime();
        new AwaitingOf(
            () -> attempts.incrementAndGet() >= 3,
            Duration.ofMillis(20)
        ).perform();
        final var elapsed = System.nanoTime() - start;
        assertTrue(elapsed > 30_000_000L);
    }
}
