package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.charset.StandardCharsets;

public final class StatusTest extends TestCase {
    public void testStatus200() {
        final var status = new Status(
            new Bytes.Of(
                "HTTP/1.1 200 OK\r\nContent-Length: 5\r\n\r\nHello"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(200, status.value());
    }

    public void testStatus302() {
        final var status = new Status(
            new Bytes.Of(
                "HTTP/1.1 302 Found\r\nLocation: /new\r\n\r\n"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(302, status.value());
    }

    public void testStatus404() {
        final var status = new Status(
            new Bytes.Of(
                "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(404, status.value());
    }

    public void testStatusMissing() {
        final var status = new Status(
            new Bytes.Of(
                "Content-Length: 5\r\n\r\nHello"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        try {
            status.value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyBytes() {
        final var status = new Status(
            new Bytes.Of(new byte[0])
        );
        try {
            status.value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNonPositiveStatus() {
        final var status = new Status(
            new Bytes.Of(
                "HTTP/1.1 -10 WHAT\r\nContent-Length: 0\r\n\r\n"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        try {
            status.value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNotThreeDigitStatus() {
        final var status = new Status(
            new Bytes.Of(
                "HTTP/1.1 10 WHAT\r\nContent-Length: 0\r\n\r\n"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        try {
            status.value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}