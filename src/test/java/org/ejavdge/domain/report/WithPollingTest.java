package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class WithPollingTest extends TestCase {
    public void testWithAlwaysPositiveVerdict() {
        final var calls = new AtomicInteger(0);
        new WithPolling(
            calls::incrementAndGet,
            () -> true,
            Duration.ZERO
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testRepeatingUntilVerdictIsOk() {
        final var calls = new AtomicInteger(0);
        final var attempts = new AtomicInteger(0);
        new WithPolling(
            calls::incrementAndGet,
            () -> attempts.incrementAndGet() >= 10,
            Duration.ZERO
        ).perform();
        assertTrue(
            calls.get() == 1
                && attempts.get() == 10
        );
    }

    public void testBrokenOrigin() {
        try {
            new WithPolling(
                () -> {
                    throw new InvariantViolation(
                        "Effect can not be performed"
                    );
                },
                () -> true,
                Duration.ZERO
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenVerdict() {
        final var calls = new AtomicInteger(0);
        try {
            new WithPolling(
                calls::incrementAndGet,
                () -> {
                    throw new InvariantViolation(
                        "There is no info about verdict"
                    );
                },
                Duration.ZERO
            ).perform();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(0, calls.get());
        }
    }

    public void testDelaysBetweenAttempts() {
        final var attempts = new AtomicInteger(0);
        final var start = System.nanoTime();
        new WithPolling(
            () -> {},
            () -> attempts.incrementAndGet() >= 3,
            Duration.ofMillis(20)
        ).perform();
        final var elapsed = System.nanoTime() - start;
        assertTrue(elapsed > 30_000_000L);
    }

    public void testOrderOfCalls() {
        final var builder = new StringBuilder();
        new WithPolling(
            () -> builder.append("Origin"),
            () -> {
                builder.append("Verdict");
                return true;
            },
            Duration.ZERO
        ).perform();
        assertEquals("VerdictOrigin", builder.toString());
    }
}
