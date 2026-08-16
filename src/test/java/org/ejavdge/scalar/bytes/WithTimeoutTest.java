package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class WithTimeoutTest extends TestCase {
    public void testCompletesWithinTimeout() {
        assertEquals(
            "HelloWorld",
            new String(
                new WithTimeout(
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
        try {
            new WithTimeout(
                () -> {
                    try {
                        Thread.sleep(500L);
                    } catch (final InterruptedException err) {
                        Thread.currentThread().interrupt();
                    }
                    return new byte[0];
                },
                Duration.ofMillis(10)
            ).content();
        } catch (final InvariantViolation err) {
            assertTrue(err.getMessage().contains("must be obtained within"));
            return;
        }
        fail("InvariantViolation");
    }

    public void testPreservesInvariantViolation() {
        final InvariantViolation expected = new InvariantViolation("original");
        try {
            new WithTimeout(
                () -> {
                    throw expected;
                },
                Duration.ofSeconds(1)
            ).content();
        } catch (final InvariantViolation actual) {
            assertEquals(expected, actual.getCause().getCause());
            return;
        }
        fail("InvariantViolation");
    }
}