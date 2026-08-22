package org.ejavdge.contest;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.body.multipart.Part;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class ContestFormTest extends TestCase {
    public void testValidForm() {
        try {
            new ContestForm(
                new MultipartDriver(
                    """
                    POST /ejudge HTTP/1.1\r
                    Host: localhost:90\r
                    Cookie: EJSID=756b423a0a6fe6a7\r
                    Content-Type: multipart/form-data; boundary={BOUNDARY}\r
                    Content-Length: 188\r
                    \r
                    --{BOUNDARY}\r
                    Content-Disposition: form-data; name="SID"\r
                    \r
                    1684bb4a0f94302c\r
                    --{BOUNDARY}--\r
                    """
                ),
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("localhost"),
                    new Num.Of(90)
                ),
                new Session(
                    new Bytes.Of(
                        """
                        HTTP/1.1 302 FOUND\r
                        Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                        Location: http://0.0.0.0:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                        Content-Length: 2\r
                        \r
                        OK\r
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).send();
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testWithoutEjsid() {
        try {
            new ContestForm(
                (loc, req) -> new byte[0],
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("localhost"),
                    new Num.Of(90)
                ),
                new Session(
                    new Bytes.Of(
                        """
                        HTTP/1.1 302 FOUND\r
                        Location: http://0.0.0.0:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                        Content-Length: 2\r
                        \r
                        OK\r
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).send();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutSid() {
        try {
            new ContestForm(
                (loc, req) -> new byte[0],
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("localhost"),
                    new Num.Of(90)
                ),
                new Session(
                    new Bytes.Of(
                        """
                        HTTP/1.1 302 FOUND\r
                        Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                        Location: http://0.0.0.0:90/ejudge?action=2&lt=1\r
                        Content-Length: 2\r
                        \r
                        OK\r
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).send();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testSessionEval() {
        final var calls = new AtomicInteger(0);
        new ContestForm(
            (loc, req) -> {
                req.bytes();
                return """
                    HTTP/1.1 302 FOUND\r
                    Host: localhost:90\r
                    Content-Length: 5\r
                    \r
                    Sent!\r
                    """.getBytes(StandardCharsets.UTF_8);
            },
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("localhost"),
                new Num.Of(90)
            ),
            new Session(
                () -> {
                    calls.incrementAndGet();
                    return """
                        HTTP/1.1 302 FOUND\r
                        Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                        Location: http://0.0.0.0:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                        Content-Length: 2\r
                        \r
                        OK\r
                        """.getBytes(StandardCharsets.UTF_8);
                }
            )
        ).send();
        assertEquals(1, calls.get());
    }

    public void testCompose() {
        try {
            new ContestForm(
                new ContestForm(
                    new MultipartDriver(
                       """
                       POST /ejudge HTTP/1.1\r
                       Host: localhost:90\r
                       Cookie: EJSID=756b423a0a6fe6a7\r
                       Content-Type: multipart/form-data; boundary={BOUNDARY}\r
                       Content-Length: 303\r
                       \r
                       --{BOUNDARY}\r
                       Content-Disposition: form-data; name="SID"\r
                       \r
                       1684bb4a0f94302c\r
                       --{BOUNDARY}\r
                       Content-Disposition: form-data; name="prob_id"\r
                       \r
                       1\r
                       --{BOUNDARY}--\r
                       """
                    ),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://0.0.0.0:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                new Items.Of<>(
                    new Part.Of(
                        new Bytes.Of(
                            """
                            Content-Disposition: form-data; name="prob_id"\r
                            \r
                            1""".getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).send();
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }
}
