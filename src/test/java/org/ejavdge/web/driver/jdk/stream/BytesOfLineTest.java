package org.ejavdge.web.driver.jdk.stream;

import junit.framework.TestCase;

import static org.junit.Assert.assertArrayEquals;

public final class BytesOfLineTest extends TestCase {
    public void testBytesOfLine() {
        final int[] input = {72, 101, 108, 108, 111};
        final BytesOfLine bytes = new BytesOfLine(input);
        final byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, bytes.content());
    }

    public void testEmptyLine() {
        final int[] input = {};
        final BytesOfLine bytes = new BytesOfLine(input);
        assertEquals(0, bytes.content().length);
    }

    public void testLineWithZero() {
        final int[] input = {0, 1, 2};
        final BytesOfLine bytes = new BytesOfLine(input);
        final byte[] expected = { 0, 1, 2 };
        assertArrayEquals(expected, bytes.content());
    }
}
