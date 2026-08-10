package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class TransferEncodingTest extends TestCase {
    public void testTransferEncoding() {
        final Text encoding = new TransferEncoding(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                Transfer-Encoding: chunked
                Server: nginx
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals("chunked", encoding.content());
    }

    public void testLowercaseHeader() {
        final Text encoding = new TransferEncoding(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                transfer-encoding: chunked
                Server: nginx
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals("chunked", encoding.content());
    }

    public void testMixedCaseHeader() {
        final Text encoding = new TransferEncoding(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                trAnsFer-ENcoDing: chunked
                Server: nginx
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals("chunked", encoding.content());
    }

    public void testMissingTransferEncoding() {
        final Text encoding = new TransferEncoding(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                Server: nginx
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        try {
            encoding.content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyBytes() {
        final Text encoding = new TransferEncoding(
            new Bytes.Of(new byte[0])
        );
        try {
            encoding.content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testTransferEncodingWithSpaces() {
        final Text encoding = new TransferEncoding(
            new Bytes.Of(
                """
                HTTP/1.1 200 OK
                Transfer-Encoding:   chunked
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
        assertEquals("chunked", encoding.content());
    }
}
