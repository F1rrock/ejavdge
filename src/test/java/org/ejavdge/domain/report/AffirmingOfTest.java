package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class AffirmingOfTest extends TestCase {
    public void testOkVerdict() {
        try {
            new AffirmingOf(
                () -> true,
                new Text.Of("Affirmation failed")
            ).perform();
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testNonOkVerdict() {
        try {
            new AffirmingOf(
                () -> false,
                new Text.Of("Affirmation failed")
            ).perform();
        } catch (final InvariantViolation e) {
            assertEquals("Affirmation failed", e.getMessage());
            return;
        }
        fail("InvariantViolation");
    }

    public void testMessageCallsOnOkVerdict() {
        final var calls = new AtomicInteger(0);
        new AffirmingOf(
            () -> true,
            () -> {
                calls.incrementAndGet();
                return "Sample text";
            }
        ).perform();
        assertEquals(0, calls.get());
    }

    public void testMessageCallsOnNonOkVerdict() {
        final var calls = new AtomicInteger(0);
        try {
            new AffirmingOf(
                () -> false,
                () -> {
                    calls.incrementAndGet();
                    return "Affirmation failed";
                }
            ).perform();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(1, calls.get());
        }
    }

    public void testVerdictCalls() {
        final var calls = new AtomicInteger(0);
        new AffirmingOf(
            () -> {
                calls.incrementAndGet();
                return true;
            },
            new Text.Of("Affirmation failed")
        ).perform();
        assertEquals(1, calls.get());
    }
}
