package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class WithRetriesTest extends TestCase {
    public void testNoRetryOnSuccess() {
        final var calls = new AtomicInteger();
        new WithRetries(
            () -> {
                calls.incrementAndGet();
                return new byte[0];
            },
            new Num.Of(5)
        ).content();
        assertEquals(1, calls.get());
    }

    public void testOriginalBytes() {
        assertEquals(
            "OK",
            new String(
                new WithRetries(
                    new Bytes.Of(
                        "OK".getBytes(StandardCharsets.UTF_8)
                    ),
                    new Num.Of(5)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testRetryAfterFailure() {
        final var calls = new AtomicInteger();
        new WithRetries(
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

    public void testExhaustion() {
        try {
            new WithRetries(
                () -> {
                    throw new InvariantViolation("failed");
                },
                new Num.Of(5)
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNonPositiveAttempts() {
        try {
            new WithRetries(
                new Bytes.Of(new byte[0]),
                new Num.Of(0)
            ).content();
        } catch (final InvariantViolation err) {
            return;
        }
        fail("InvariantViolation");
    }
}
