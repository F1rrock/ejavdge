package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.WithRetries;
import org.ejavdge.scalar.num.Num;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class HasStatusTest extends TestCase {
    public void testExpectedStatus200() {
        try {
            new HasStatus(
                new Num.Of(200),
                new Bytes.Of(
                    "HTTP/1.1 200 OK\r\nContent-Length: 5\r\n\r\nHello"
                        .getBytes(StandardCharsets.UTF_8)
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
                    "HTTP/1.1 302 Found\r\nLocation: /new\r\n\r\n"
                        .getBytes(StandardCharsets.UTF_8)
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
                    "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n"
                        .getBytes(StandardCharsets.UTF_8)
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
                    "HTTP/1.1 302 Found\r\nLocation: /new\r\n\r\n"
                        .getBytes(StandardCharsets.UTF_8)
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
                    "Content-Length: 5\r\n\r\nHello"
                        .getBytes(StandardCharsets.UTF_8)
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
                    return "HTTP/1.1 200 OK\r\nLocation: /new\r\n\r\n"
                        .getBytes(StandardCharsets.UTF_8);
                }
            ).content();
            assertEquals(1, calls.get());
        } catch (final InvariantViolation e) {
            fail("InvariantViolation");
        }
    }
}
