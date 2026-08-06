package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.driver.jdk.stream.ByteStream;

import java.nio.charset.StandardCharsets;

public final class BodyOfTest extends TestCase {
    public void testExactBody() {
        assertEquals(
            "Hello",
            new String(
                new BodyOf(
                    response(
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: 5\r
                        \r
                        Hello\
                        """
                    ),
                    new Num.Of(5)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testContentLengthRespect() {
        assertEquals(
            "Hel",
            new String(
                new BodyOf(
                    response(
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: 5\r
                        \r
                        Hello\
                        """
                    ),
                    new Num.Of(3)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testEmptyBody() {
        assertEquals(
            "",
            new String(
                new BodyOf(
                    response(
                        """
                        HTTP/1.1 204 No Content\r
                        \r\
                        """
                    ),
                    new Num.Of(0)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testMultilineBody() {
        assertEquals(
            "Hello\nWorld",
            new String(
                new BodyOf(
                    response(
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: 11\r
                        \r
                        Hello
                        World\
                        """
                    ),
                    new Num.Of(11)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    private static HttpResponse response(final String raw) {
        return new HttpResponse(
            new ByteStream.Of(raw.chars())
        );
    }
}
