package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.web.driver.jdk.stream.ByteStream;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public final class HttpResponseTest extends TestCase {
    public void testHeaders() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                Content-Length: 5\r
                \r
                Hello\
                """.chars()
            )
        );
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Content-Length: 5\r
            """,
            utf8(response.headers())
        );
    }

    public void testBody() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                Content-Length: 5\r
                \r
                Hello\
                """.chars()
            )
        );
        assertEquals(
            "Hello",
            utf8(response.body().limit(5))
        );
    }

    public void testAllHeaders() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                Header-A: aaa\r
                Header-B: bbb\r
                Header-C: ccc\r
                \r
                Body\
                """.chars()
            )
        );
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Header-A: aaa\r
            Header-B: bbb\r
            Header-C: ccc\r
            """,
            utf8(response.headers())
        );
    }

    public void testBodyBeforeHeaders() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                Content-Length: 5\r
                \r
                Hello\
                """.chars()
            )
        );
        response.body()
            .limit(5)
            .forEach(ignored -> {});
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Content-Length: 5\r
            """,
            utf8(response.headers())
        );
    }

    public void testEmptyBody() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                \r
                """.chars()
            )
        );
        assertEquals("", utf8(response.body()));
    }

    public void testEmptyHeaders() {
        final var response = new HttpResponse(
            new ByteStream.Of("\r\n".chars())
        );
        assertEquals("", utf8(response.headers()));
    }

    public void testMultipleHeaders() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                Header-A: value1\r
                Header-B: value2\r
                Header-C: value3\r
                \r
                Body
                """.chars()
            )
        );
        final var headers = utf8(response.headers());
        assertEquals(
        """
            HTTP/1.1 200 OK\r
            Header-A: value1\r
            Header-B: value2\r
            Header-C: value3\r
            """,
            headers
        );
    }

    public void testStatusBody() {
        final var response = new HttpResponse(
            new ByteStream.Of(
                 """
                 HTTP/1.1 200 OK\r
                 Date: Tue, 29 Sep 2026 12:01:58 GMT\r
                 Server: Apache/2.4.52 (Ubuntu)\r
                 Cache-Control: no-cache\r
                 Content-Length: 65\r
                 Content-Type: text/plain; charset=utf-8\r
                 \r
                 { "h": 15, "m": 23, "s": 31, "d": 29, "o": 9, "y": 2026, "z": 1 }""".chars()
            )
        );
        assertEquals(
            """
            { "h": 15, "m": 23, "s": 31, "d": 29, "o": 9, "y": 2026, "z": 1 }""",
            utf8(response.body().limit(65))
        );
    }

    private static String utf8(final byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String utf8(final IntStream src) {
        return src.collect(
            ByteArrayOutputStream::new,
            ByteArrayOutputStream::write,
            (a, b) -> {
            }
        ).toString(StandardCharsets.UTF_8);
    }
}
