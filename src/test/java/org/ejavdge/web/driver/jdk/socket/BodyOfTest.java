package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.driver.jdk.stream.ByteStream;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

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

    public void testZeroContentLength() {
        final var body = new BodyOf(
            response(
                """
                HTTP/1.1 204 No Content\r
                Content-Length: 0\r
                \r\
                """
            ),
            new Num.Of(0)
        );
        assertEquals(0, body.content().length);
    }

    public void testBodyWithUtf8Characters() {
        final var bodyText = "Привет";
        final var bodyBytes = bodyText.getBytes(StandardCharsets.UTF_8);
        final var contentLength = bodyBytes.length;
        final var body = new BodyOf(
            response(
                String.format(
                    "HTTP/1.1 200 OK\r%nContent-Length: %d\r%n\r%n%s",
                    contentLength,
                    bodyText
                )
            ),
            new Num.Of(contentLength)
        );
        assertEquals(
            bodyText,
            new String(body.content(), StandardCharsets.UTF_8)
        );
    }

    private static HttpResponse response(final String raw) {
        final byte[] bytes = raw.getBytes(StandardCharsets.UTF_8);
        return new HttpResponse(
            new ByteStream.Of(
                IntStream.range(0, bytes.length)
                    .map(i -> bytes[i] & 0xFF)
            )
        );
    }
}
