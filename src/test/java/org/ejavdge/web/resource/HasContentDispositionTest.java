package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public final class HasContentDispositionTest extends TestCase {
    public void testMatchingDispositionAndResponseBytes() {
        final var calls = new AtomicInteger();
        final byte[] response = this.response("Attachment; filename=\"HeaderOfA.java\"");
        assertTrue(
            Arrays.equals(
                response,
                new HasContentDisposition(
                    new Text.Of("attachment"),
                    () -> {
                        calls.incrementAndGet();
                        return response.clone();
                    }
                ).content()
            )
        );
        assertEquals(1, calls.get());
    }

    public void testDispositionWithMatchingSubstring() {
        try {
            new HasContentDisposition(
                new Text.Of("attachment"),
                new Bytes.Of(this.response("x-attachment"))
            ).content();
        } catch (final InvariantViolation e) {
            assertTrue(e.getMessage().contains("x-attachment"));
            return;
        }
        fail("InvariantViolation");
    }

    public void testCustomMessageOnDispositionMismatch() {
        final var message = "There is no attachment.";
        try {
            new HasContentDisposition(
                new Text.Of("attachment"),
                new Text.Of(message),
                new Bytes.Of(this.response("inline"))
            ).content();
        } catch (final InvariantViolation e) {
            assertEquals(message, e.getMessage());
            return;
        }
        fail("InvariantViolation");
    }

    private byte[] response(final String disposition) {
        return """
            HTTP/1.1 200 OK\r
            Content-Disposition: %s\r
            \r
            Response body
            """.formatted(disposition).getBytes(StandardCharsets.UTF_8);
    }
}
