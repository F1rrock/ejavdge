package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class WithRetryTest extends TestCase {
    public void testSuccessfulContentStopsFurtherAttempts() {
        final AtomicInteger calls = new AtomicInteger();
        new WithRetry(
            () -> {
                calls.incrementAndGet();
                return "OK".getBytes(StandardCharsets.UTF_8);
            },
            new Num.Of(5)
        ).content();
        assertEquals(1, calls.get());
    }

    public void testSuccessfulContentReturnsOriginBytes() {
        assertEquals(
            "OK",
            this.text(
                new WithRetry(
                    new Bytes.Of(
                        "OK".getBytes(StandardCharsets.UTF_8)
                    ),
                    new Num.Of(5)
                ).content()
            )
        );
    }

    public void testFailedContentRetriesOrigin() {
        final AtomicInteger calls = new AtomicInteger();
        new WithRetry(
            () -> {
                if (calls.incrementAndGet() < 3) {
                    throw new InvariantViolation("failed");
                }
                return "OK".getBytes(StandardCharsets.UTF_8);
            },
            new Num.Of(5)
        ).content();
        assertEquals(3, calls.get());
    }

    public void testExhaustedAttemptsPreserveFailureChain() {
        final AtomicInteger calls = new AtomicInteger();
        final InvariantViolation expected = new InvariantViolation("failed");
        try {
            new WithRetry(
                () -> {
                    calls.incrementAndGet();
                    throw expected;
                },
                new Num.Of(5)
            ).content();
        } catch (final InvariantViolation actual) {
            assertSame(expected, this.rootCause(actual));
            return;
        }
        fail("InvariantViolation");
    }

    public void testExhaustedAttemptsEvaluateOriginFiveTimes() {
        final AtomicInteger calls = new AtomicInteger();
        try {
            new WithRetry(
                () -> {
                    calls.incrementAndGet();
                    throw new InvariantViolation("failed");
                },
                new Num.Of(5)
            ).content();
        } catch (final InvariantViolation err) {
            assertEquals(5, calls.get());
            return;
        }
        fail("InvariantViolation");
    }

    public void testNonPositiveAttemptsFailDuringContentEvaluation() {
        final WithRetry retry = new WithRetry(
            new Bytes.Of(new byte[0]),
            new Num.Of(0)
        );
        try {
            retry.content();
        } catch (final InvariantViolation err) {
            assertTrue(err.getMessage().contains("positive"));
            return;
        }
        fail("InvariantViolation");
    }

    private String text(final byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private InvariantViolation rootCause(final InvariantViolation err) {
        InvariantViolation result = err;
        while (result.getCause() instanceof InvariantViolation) {
            result = (InvariantViolation) result.getCause();
        }
        return result;
    }
}