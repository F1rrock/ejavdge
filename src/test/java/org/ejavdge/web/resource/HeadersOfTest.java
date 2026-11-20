package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class HeadersOfTest extends TestCase {
    public void testHeadersBeforeBinaryBody() {
        final byte[] headers = (
            "HTTP/1.1 200 OK\r\nContent-Disposition: attachment; filename=\"пример.java\""
        ).getBytes(StandardCharsets.UTF_8);
        assertTrue(
            Arrays.equals(
                headers,
                new HeadersOf(
                    new Concat(
                        new Bytes.Of(headers),
                        new Bytes.Of("\r\n\r\n".getBytes(StandardCharsets.UTF_8)),
                        new Bytes.Of(new byte[]{(byte) 0xff, '\r', '\n', '\r', '\n', 0})
                    )
                ).content()
            )
        );
    }

    public void testResponseWithoutHeaderTerminator() {
        try {
            new HeadersOf(
                new Bytes.Of("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8))
            ).content();
        } catch (final InvariantViolation e) {
            assertTrue(e.getMessage().contains("header terminator"));
            return;
        }
        fail("InvariantViolation");
    }
}
