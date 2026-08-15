package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;

import java.nio.charset.StandardCharsets;

public final class ContentLengthTest extends TestCase {
    public void testContentLength() {
        final Num length = new ContentLength(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                Content-Length: 42
                Server: nginx
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(42, length.value());
    }

    public void testLowercaseHeader() {
        final Num length = new ContentLength(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                content-length: 123
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(123, length.value());
    }

    public void testMixedCaseHeader() {
        final Num length = new ContentLength(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                CoNtEnT-LeNgTh: 999
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(999, length.value());
    }

    public void testMissingContentLength() {
        final Num length = new ContentLength(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                Server: nginx
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        try {
            length.value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyBytes() {
        final Num length = new ContentLength(
            new Bytes.Of(new byte[0])
        );
        try {
            length.value();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testContentLengthWithSpaces() {
        final Num length = new ContentLength(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                Content-Length:   100
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(100, length.value());
    }

    public void testContentLengthZero() {
        final Num length = new ContentLength(
            new Bytes.Of(
                """
                HTTP/1.1 204 No Content
                Content-Length: 0
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals(0, length.value());
    }
}