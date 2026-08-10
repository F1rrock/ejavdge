package org.ejavdge.web.driver.jdk.stream;

import junit.framework.TestCase;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

public final class ConcatOfLinesTest extends TestCase {

    public void testSingleLine() {
        Stream<int[]> lines = Stream.of(new int[]{1, 2, 3});
        ByteStream concat = new ConcatOfLines(lines);
        int[] result = concat.content().toArray();
        assertArrayEquals(new int[]{1, 2, 3, 10}, result);
    }

    public void testMultipleLines() {
        Stream<int[]> lines = Stream.of(
            new int[]{1, 2},
            new int[]{3, 4}
        );
        ByteStream concat = new ConcatOfLines(lines);
        int[] result = concat.content().toArray();
        assertArrayEquals(new int[]{1, 2, 10, 3, 4, 10}, result);
    }

    public void testEmptyLines() {
        Stream<int[]> lines = Stream.empty();
        ByteStream concat = new ConcatOfLines(lines);
        int[] result = concat.content().toArray();
        assertEquals(0, result.length);
    }

    public void testCustomSeparator() {
        Stream<int[]> lines = Stream.of(new int[]{5, 6});
        ByteStream concat = new ConcatOfLines(';', lines);
        int[] result = concat.content().toArray();
        assertArrayEquals(new int[]{5, 6, 59}, result);
    }

    public void testLineWithEmptyArray() {
        Stream<int[]> lines = Stream.of(new int[]{});
        ByteStream concat = new ConcatOfLines(lines);
        int[] result = concat.content().toArray();
        assertArrayEquals(new int[]{10}, result);
    }
}
