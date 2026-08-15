package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class RetryTest extends TestCase {
    public void testSuccessStopsFurtherAttempts() {
        final AtomicInteger calls = new AtomicInteger();
        final byte[] result = new Retry(() -> {
            calls.incrementAndGet();
            return new Bytes.Of("OK".getBytes(StandardCharsets.UTF_8));
        }, 5).content();

        assertEquals("OK", new String(result, StandardCharsets.UTF_8));
        assertEquals(1, calls.get());
    }

    public void testFailureIsRetriedWithFreshBytes() {
        final AtomicInteger factories = new AtomicInteger();
        final AtomicInteger calls = new AtomicInteger();
        final byte[] result = new Retry(() -> {
            factories.incrementAndGet();
            return () -> {
                if (calls.incrementAndGet() < 3) {
                    throw new InvariantViolation("failed");
                }
                return "OK".getBytes(StandardCharsets.UTF_8);
            };
        }, 5).content();

        assertEquals("OK", new String(result, StandardCharsets.UTF_8));
        assertEquals(3, factories.get());
        assertEquals(3, calls.get());
    }

    public void testFinalFailureIsPropagated() {
        final AtomicInteger factories = new AtomicInteger();
        final InvariantViolation expected = new InvariantViolation("failed");

        try {
            new Retry(() -> {
                factories.incrementAndGet();
                return () -> {
                    throw expected;
                };
            }, 5).content();
        } catch (final InvariantViolation actual) {
            assertEquals(expected, actual);
            assertEquals(5, factories.get());
            return;
        }

        fail("InvariantViolation");
    }

    public void testAttemptsMustBePositive() {
        try {
            new Retry(new Bytes.Of(new byte[0]), 0);
        } catch (final IllegalArgumentException err) {
            return;
        }

        fail("IllegalArgumentException");
    }
}