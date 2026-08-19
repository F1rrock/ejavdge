package org.ejavdge.contest;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.WithEntry;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class ContestResourceTest extends TestCase {
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
            new String(
                new ContestResource(
                    new FakeDriver(
                        """
                        GET /ejudge?SID=1684bb4a0f94302c HTTP/1.1\r
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
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testInvalidResource() {
        assertEquals(
        """
            HTTP/1.1 200 OK\r
            Transfer-Encoding: chunked\r
            \r
            15\r
            Invalid session\r
            0\r
            """,
            new String(
                new ContestResource(
                    new FakeDriver(
                        """
                        GET /ejudge?SID=1684bb4a0f94302c HTTP/1.1\r
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
                            Location: http://0.0.0.0:90/ejudge?SID=0000000000000000&action=2&lt=1\r
                            Content-Length: 4\r
                            \r
                            FAIL\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testComposition() {
        assertEquals(
        """
            HTTP/1.1 200 OK\r
            Transfer-Encoding: chunked\r
            \r
            7\r
            Success\r
            0\r
            """,
            new String(
                new ContestResource(
                    new ContestResource(
                        new FakeDriver(
                            """
                            GET /ejudge?a=b&SID=1684bb4a0f94302c HTTP/1.1\r
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
                                Content-Length: 4\r
                                \r
                                FAIL\r
                                """.getBytes(StandardCharsets.UTF_8)
                            )
                        )
                    ),
                    new WithEntry(
                        new Text.Of("a"),
                        new Text.Of("b")
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testInvalidSession() {
        try {
            new ContestResource(
                new FakeDriver(
                    """
                    GET /ejudge?SID=1684bb4a0f94302c HTTP/1.1\r
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
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testSessionEval() {
        final var calls = new AtomicInteger(0);
        try {
            new ContestResource(
                new FakeDriver(
                    """
                    GET /ejudge?SID=1684bb4a0f94302c HTTP/1.1\r
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
                    () -> {
                        calls.incrementAndGet();
                        return """
                            HTTP/1.1 200 OK\r
                            Location: http://0.0.0.0:90/ejudge\r
                            Content-Length: 15\r
                            \r
                            Invalid Session\r
                            """.getBytes(StandardCharsets.UTF_8);
                    }
                )
            ).content();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(1, calls.get());
        }
    }
}
