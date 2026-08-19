package org.ejavdge.contest;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.RunId;

import java.nio.charset.StandardCharsets;

public final class ReportPageTest extends TestCase {
    public void testValidResource() {
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Transfer-Encoding: chunked\r
            \r
            7\r
            Success\r
            0\r
            """,
            new ReportPage(
                new ContestResource(
                    new FakeDriver(
                        """
                        GET /ejudge?action=37&run_id=1&SID=1684bb4a0f94302c HTTP/1.1\r
                        Host: 0.0.0.0:90\r
                        Cookie: EJSID=756b423a0a6fe6a7\r
                        \r
                        """.getBytes(StandardCharsets.UTF_8),
                        """
                        HTTP/1.1 200 OK\r
                        Transfer-Encoding: chunked\r
                        \r
                        15\r
                        Invalid session\r
                        0\r
                        """.getBytes(StandardCharsets.UTF_8),
                        """
                        HTTP/1.1 200 OK\r
                        Transfer-Encoding: chunked\r
                        \r
                        7\r
                        Success\r
                        0\r
                        """.getBytes(StandardCharsets.UTF_8)
                    ),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("0.0.0.0"),
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
                new RunId(1)
            ).content()
        );
    }

    public void testInvalidSession() {
        try {
            new ReportPage(
                new ContestResource(
                    new FakeDriver(
                        """
                        GET /ejudge?action=37&run_id=1&SID=1684bb4a0f94302c HTTP/1.1\r
                        Host: 0.0.0.0:90\r
                        Cookie: EJSID=756b423a0a6fe6a7\r
                        \r
                        """.getBytes(StandardCharsets.UTF_8),
                        """
                        HTTP/1.1 200 OK\r
                        Transfer-Encoding: chunked\r
                        \r
                        15\r
                        Invalid session\r
                        0\r
                        """.getBytes(StandardCharsets.UTF_8),
                        """
                        HTTP/1.1 200 OK\r
                        Transfer-Encoding: chunked\r
                        \r
                        7\r
                        Success\r
                        0\r
                        """.getBytes(StandardCharsets.UTF_8)
                    ),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("0.0.0.0"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 200 OK\r
                            Location: http://0.0.0.0:90/ejudge\r
                            Content-Length: 15\r
                            \r
                            Invalid Session\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                new RunId(1)
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
