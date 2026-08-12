package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class TimeoutTest extends TestCase {
    public void testCompleteWithinTimeout() {
        assertEquals(
            "HelloWorld",
            new String(
                new Timeout(
                    new Bytes.Of(
                        "HelloWorld".getBytes(StandardCharsets.UTF_8)
                    ),
                    Duration.ofSeconds(1)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testTimeout() {
        final Bytes slow = () -> {
            try {
                Thread.sleep(500L);
            } catch (final InterruptedException err) {
                throw new RuntimeException(err);
            }
            return new byte[0];
        };

        try {
            new Timeout(slow, Duration.ofMillis(10)).content();
        } catch (final InvariantViolation err) {
            assertTrue(err.getMessage().contains("timed out"));
            return;
        }
        fail("InvariantViolation");
    }

    public void testWrappedInvariantViolationIsPreserved() {
        final InvariantViolation expected = new InvariantViolation("original");
        final Bytes failing = () -> {
            throw expected;
        };

        try {
            new Timeout(failing, Duration.ofSeconds(1)).content();
        } catch (final InvariantViolation actual) {
            assertEquals(expected, actual);
            return;
        }
        fail("InvariantViolation");
    }
}