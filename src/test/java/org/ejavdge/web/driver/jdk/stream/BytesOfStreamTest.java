package org.ejavdge.web.driver.jdk.stream;

import junit.framework.TestCase;

import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public final class BytesOfStreamTest extends TestCase {

    public void testEmpty() {
        assertEquals(
            0,
            new BytesOfStream("".chars()).content().length
        );
    }

    public void testOneByte() {
        assertArrayEquals(
            new byte[] { 65 },
            new BytesOfStream(IntStream.of(65)).content()
        );
    }

    public void testSeveralBytes() {
        assertArrayEquals(
            new byte[] { 65, 66, 67 },
            new BytesOfStream(IntStream.of(65, 66, 67)).content()
        );
    }

    public void testWithEncoding() {
        assertEquals(
            "Hello, world!",
            new String(
                new BytesOfStream("Hello, world!".chars()).content(),
                java.nio.charset.StandardCharsets.UTF_8
            )
        );
    }
}
