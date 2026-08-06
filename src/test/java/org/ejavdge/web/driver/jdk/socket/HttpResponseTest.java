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
            new ByteStream.Of("".chars())
        );
        assertEquals("", utf8(response.body().limit(0)));
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
                Body\
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

    private static String utf8(final IntStream src) {
        return src.collect(
            ByteArrayOutputStream::new,
            ByteArrayOutputStream::write,
            (a, b) -> {
            }
        ).toString(StandardCharsets.UTF_8);
    }
}
