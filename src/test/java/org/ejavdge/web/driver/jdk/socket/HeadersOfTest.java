package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.web.driver.jdk.stream.ByteStream;

import java.nio.charset.StandardCharsets;

public final class HeadersOfTest extends TestCase {
    public void testStatusLine() {
        assertEquals(
            "HTTP/1.1 200 OK\r\n",
            new String(
                new HeadersOf(
                    response(
                        """
                        HTTP/1.1 200 OK\r
                        \r\
                        """
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testHeaderFields() {
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Content-Length: 5\r
            Content-Type: text/plain\r
            """,
            new String(
                new HeadersOf(
                    response(
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: 5\r
                        Content-Type: text/plain\r
                        \r
                        Hello\
                        """
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testHeadersWithoutBody() {
        assertEquals(
            """
            HTTP/1.1 200 OK\r
            Content-Length: 5\r
            """,
            new String(
                new HeadersOf(
                    response(
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: 5\r
                        \r
                        Hello\
                        """
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testTrailingCrlf() {
        assertTrue(
            new String(
                new HeadersOf(
                    response(
                        """
                        HTTP/1.1 204 No Content\r
                        \r\
                        """
                    )
                ).content(),
                StandardCharsets.UTF_8
            ).endsWith("\r\n")
        );
    }

    public void testHeadersWithMultipleEmptyLines() {
        final var headers = new HeadersOf(
            response(
                """
                HTTP/1.1 200 OK\r
                \r
                \r
                Hello\
                """
            )
        );
        final var result = new String(headers.content(), StandardCharsets.UTF_8);
        assertEquals("HTTP/1.1 200 OK\r\n", result);
    }

    public void testHeadersWithExtraSpaces() {
        final var headers = new HeadersOf(
            response(
                """
                HTTP/1.1 200 OK\r
                Content-Length:   5   \r
                \r
                Hello\
                """
            )
        );
        final var result = new String(headers.content(), StandardCharsets.UTF_8);
        assertTrue(result.contains("Content-Length:   5   ")); // Сохраняем пробелы
    }

    private static HttpResponse response(final String raw) {
        return new HttpResponse(
            new ByteStream.Of(raw.chars())
        );
    }
}
