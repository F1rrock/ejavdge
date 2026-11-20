package org.ejavdge.contest;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.driver.WebDriver;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public final class AttachmentTest extends TestCase {
    public void testBinaryAttachmentPayload() {
        final byte[] payload = {0, (byte) 0xff, '\r', '\n', 42};
        assertArrayEquals(payload, this.attachment(
            (loc, req) -> {
                assertTrue(
                    new String(req.bytes(), StandardCharsets.UTF_8).startsWith(
                        "GET /ejudge?action=194&file=HeaderOfA.java&prob_id=3&SID=abc HTTP/1.1\r\n"
                    )
                );
                return new Concat(
                    new Bytes.Of(
                        """
                            HTTP/1.1 200 OK\r
                            Content-Disposition: attachment; filename="HeaderOfA.java"\r
                            \r
                            """.getBytes(StandardCharsets.UTF_8)
                    ),
                    new Bytes.Of(payload)
                ).content();
            }
        ).content());
    }

    public void testMissingAttachmentWithSuccessfulStatus() {
        assertEquals(
            0,
            this.attachment(
                (loc, req) -> """
                    HTTP/1.1 200 OK\r
                    Content-Type: text/html; charset=UTF-8\r
                    \r
                    <html><body>Attachment not found</body></html>
                    """.getBytes(StandardCharsets.UTF_8)
            ).content().length
        );
    }

    public void testAttachmentRequestFailure() {
        try {
            this.attachment(
                (loc, req) -> {
                    throw new InvariantViolation("There is no connection to the server.");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private Attachment attachment(final WebDriver driver) {
        return new Attachment(
            new Text.Of("HeaderOfA.java"),
            new ProbId(3),
            new ContestResource(
                driver,
                new Location(new Text.Of("/ejudge"), new Text.Of("localhost"), new Num.Of(90)),
                new Session(
                    new Bytes.Of(
                        """
                        HTTP/1.1 302 Found\r
                        Set-Cookie: EJSID=def;\r
                        Location: http://localhost:90/ejudge?SID=abc&action=2&lt=1\r
                        \r
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            )
        );
    }
}
