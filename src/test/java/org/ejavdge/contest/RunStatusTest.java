package org.ejavdge.contest;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;

import java.nio.charset.StandardCharsets;

public final class RunStatusTest extends TestCase {
    public void testValidResource() {
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Transfer-Encoding: chunked\r
            \r
            75\r
            { "h": 8, "m": 8, "s": 56, "d": 19, "o": 8, "y": 2026, "r": 11667, "z": 1 }\r
            0\r
            """,
            new RunStatus(
                new ContestResource(
                    new FakeDriver(
                        """
                        GET /ejudge?action=175&x=1&SID=1684bb4a0f94302c HTTP/1.1\r
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
                        75\r
                        { "h": 8, "m": 8, "s": 56, "d": 19, "o": 8, "y": 2026, "r": 11667, "z": 1 }\r
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
                )
            ).content()
        );
    }

    public void testInvalidSession() {
        try {
            new RunStatus(
                new ContestResource(
                    new FakeDriver(
                        """
                        GET /ejudge?action=175&x=1&SID=1684bb4a0f94302c HTTP/1.1\r
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
                        75\r
                        { "h": 8, "m": 8, "s": 56, "d": 19, "o": 8, "y": 2026, "r": 11667, "z": 1 }\r
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
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
