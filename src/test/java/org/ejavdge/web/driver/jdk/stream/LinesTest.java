package org.ejavdge.web.driver.jdk.stream;

import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.IntStream;

public final class LinesTest extends TestCase {
    public void testSplitsLines() {
        final var lines = new Lines(
            new ByteStream.Of(
                IntStream.of(
                    'a', 'b', '\n',
                    'c', 'd', '\n',
                    'e', 'f'
                )
            )
        );
        final var actual = lines.content()
            .limit(3)
            .map(IntStream::of)
            .map(IntStream::boxed)
            .map(Stream::toList)
            .toList();
        assertEquals(
            List.of(
                List.of(97, 98),
                List.of(99, 100),
                List.of(101, 102)
            ),
            actual
        );
    }

    public void testEmptyLines() {
        final var lines = new Lines(
            new ByteStream.Of(
                IntStream.of(
                    'a', '\n',
                    '\n',
                    'b'
                )
            )
        );
        final var actual = lines.content()
            .limit(3)
            .map(IntStream::of)
            .map(IntStream::boxed)
            .map(Stream::toList)
            .toList();
        assertEquals(
            List.of(
                List.of(97),
                List.of(),
                List.of(98)
            ),
            actual
        );
    }

    public void testHttpResponse() {
        final var lines = new Lines(
            new ByteStream.Of(
                IntStream.generate(
                    ByteBuffer.wrap(
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: 5\r
                        \r
                        Hello
                        """.getBytes(StandardCharsets.UTF_8)
                    )::get
                )
            )
        );
        final var actual = lines.content()
            .limit(4)
            .map(IntStream::of)
            .map(IntStream::boxed)
            .map(Stream::toList)
            .toList();
        assertEquals(
            Stream.of(
                    "HTTP/1.1 200 OK\r".getBytes(StandardCharsets.UTF_8),
                    "Content-Length: 5\r".getBytes(StandardCharsets.UTF_8),
                    "\r".getBytes(StandardCharsets.UTF_8),
                    "Hello".getBytes(StandardCharsets.UTF_8)
                )
                .map(bytes -> IntStream.range(0, bytes.length)
                    .map(index -> bytes[index] & 0xFF)
                    .boxed()
                    .toList()
                )
                .toList(),
            actual
        );
    }

    public void testHttpResponseWithoutTrailingNewLine() {
        final var lines = new Lines(
            new ByteStream.Of(
                """
                HTTP/1.1 200 OK\r
                Content-Length: 5\r
                \r
                Hello""".chars()
            )
        );

        final var actual = lines.content()
            .limit(4)
            .map(IntStream::of)
            .map(IntStream::boxed)
            .map(Stream::toList)
            .toList();

        assertEquals(
            Stream.of(
                    "HTTP/1.1 200 OK\r",
                    "Content-Length: 5\r",
                    "\r",
                    "Hello"
                )
                .map(String::chars)
                .map(IntStream::boxed)
                .map(Stream::toList)
                .toList(),
            actual
        );
    }
}
