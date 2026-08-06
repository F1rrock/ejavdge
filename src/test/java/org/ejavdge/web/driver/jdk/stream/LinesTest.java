package org.ejavdge.web.driver.jdk.stream;

import junit.framework.TestCase;

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
}
