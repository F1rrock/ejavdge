package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;

import java.nio.charset.StandardCharsets;

public final class ContentDispositionTest extends TestCase {
    public void testDispositionTypeWithParameters() {
        assertEquals(
            "Attachment",
            new ContentDisposition(
                new Concat(
                    new Bytes.Of(
                        """
                        HTTP/1.1 200 OK\r
                        content-disposition:   Attachment ; filename="HeaderOfA.java"\r
                        \r
                        """.getBytes(StandardCharsets.UTF_8)
                    ),
                    new Bytes.Of(new byte[]{(byte) 0xff})
                )
            ).content()
        );
    }

    public void testDispositionTypeWithoutParameters() {
        assertEquals(
            "inline",
            new ContentDisposition(
                new Bytes.Of(
                    """
                    HTTP/1.1 200 OK\r
                    Content-Disposition: inline\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            ).content()
        );
    }

    public void testDispositionOnlyInBody() {
        try {
            new ContentDisposition(
                new Bytes.Of(
                    """
                    HTTP/1.1 200 OK\r
                    Content-Type: text/plain\r
                    \r
                    Content-Disposition: attachment\r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
