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

    public void testKeepsAllHeaders() {
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

    public void testCanReadBodyBeforeHeaders() {
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

    private static String utf8(final IntStream src) {
        return src.collect(
            ByteArrayOutputStream::new,
            ByteArrayOutputStream::write,
            (a, b) -> {
            }
        ).toString(StandardCharsets.UTF_8);
    }
}
