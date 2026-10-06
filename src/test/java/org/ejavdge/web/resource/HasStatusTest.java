package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class HasStatusTest extends TestCase {
    public void testExpectedStatus200() {
        try {
            new HasStatus(
                new Num.Of(200),
                new Bytes.Of(
                    """
                    HTTP/1.1 200 OK\r
                    Content-Length: 5\r
                    \r
                    Hello""".getBytes(StandardCharsets.UTF_8)
                )
            ).content();
        } catch (final InvariantViolation e) {
            fail("InvariantViolation");
        }
    }

    public void testExpectedStatus302() {
        try {
            new HasStatus(
                new Num.Of(302),
                new Bytes.Of(
                    """
                    HTTP/1.1 302 Found\r
                    Location: /new\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            ).content();
        } catch (final InvariantViolation e) {
            fail("InvariantViolation");
        }
    }

    public void testUnexpectedStatus404() {
        try {
            new HasStatus(
                new Num.Of(200),
                new Bytes.Of(
                    """
                    HTTP/1.1 404 Not Found\r
                    Content-Length: 0\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testUnexpectedStatus302() {
        try {
            new HasStatus(
                new Num.Of(200),
                new Bytes.Of(
                    """
                    HTTP/1.1 302 Found\r
                    Location: /new\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testStatusMissing() {
        try {
            new HasStatus(
                new Num.Of(200),
                new Bytes.Of(
                    """
                    Content-Length: 5\r
                    \r
                    Hello""".getBytes(StandardCharsets.UTF_8)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBytesEval() {
        final var calls = new AtomicInteger();
        try {
            new HasStatus(
                new Num.Of(200),
                () -> {
                    calls.incrementAndGet();
                    return """
                        HTTP/1.1 200 OK\r
                        Location: /new\r
                        \r
                        """.getBytes(StandardCharsets.UTF_8);
                }
            ).content();
            assertEquals(1, calls.get());
        } catch (final InvariantViolation e) {
            fail("InvariantViolation");
        }
    }

    public void testNoPermanentCache() {
        final var calls = new AtomicInteger();
        try {
            final var bs = new HasStatus(
                new Num.Of(200),
                () -> {
                    calls.incrementAndGet();
                    return """
                        HTTP/1.1 200 OK\r
                        Location: /new\r
                        \r
                        """.getBytes(StandardCharsets.UTF_8);
                }
            );
            bs.content();
            bs.content();
            assertEquals(2, calls.get());
        } catch (final InvariantViolation e) {
            fail("InvariantViolation");
        }
    }

    public void testCustomErrorMessage() {
        final var message = "There is invalid credentials.";
        try {
            new HasStatus(
                new Num.Of(302),
                new Text.Of(message),
                () -> """
                    HTTP/1.1 200 OK\r
                    Location: /new\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
            ).content();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(message, e.getMessage());
        }
    }

    public void testEmptyMessage() {
        try {
            new HasStatus(
                new Num.Of(302),
                new Text.Of(""),
                () -> """
                    HTTP/1.1 200 OK\r
                    Location: /new\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
            ).content();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    public void testWithoutCustomMessage() {
        try {
            new HasStatus(
                new Num.Of(302),
                () -> """
                    HTTP/1.1 200 OK\r
                    Location: /new\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
            ).content();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }
}
