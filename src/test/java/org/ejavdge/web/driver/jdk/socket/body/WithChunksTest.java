package org.ejavdge.web.driver.jdk.socket.body;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public final class WithChunksTest extends TestCase {
    public void testSingleChunk() {
        final IntStream src = IntStream.of(
            '5', '\r', '\n',
            'H', 'e', 'l', 'l', 'o', '\r', '\n',
            '0', '\r', '\n'
        );
        final WithChunks policy = new WithChunks();
        final int[] result = policy.apply(src).toArray();
        assertArrayEquals(new int[] {72, 101, 108, 108, 111}, result);
    }

    public void testMultipleChunks() {
        final IntStream src = IntStream.of(
            '3', '\r', '\n',
            'A', 'B', 'C', '\r', '\n',
            '2', '\r', '\n',
            'D', 'E', '\r', '\n',
            '0', '\r', '\n'
        );
        final WithChunks policy = new WithChunks();
        final int[] result = policy.apply(src).toArray();
        assertArrayEquals(new int[] {65, 66, 67, 68, 69}, result);
    }

    public void testChunkWithHexSize() {
        final IntStream src = IntStream.of(
            'A', '\r', '\n',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '\r', '\n',
            '0', '\r', '\n'
        );
        final WithChunks policy = new WithChunks();
        final int[] result = policy.apply(src).toArray();
        final int[] expected = IntStream.rangeClosed('0', '9').toArray();
        assertArrayEquals(expected, result);
    }

    public void testEmptyChunks() {
        final IntStream src = IntStream.of('0', '\r', '\n');
        final WithChunks policy = new WithChunks();
        final int[] result = policy.apply(src).toArray();
        assertEquals(0, result.length);
    }

    public void testChunkWithTrailingSeparator() {
        final IntStream src = IntStream.of(
            '2', '\r', '\n',
            'A', 'B', '\r', '\n',
            '0', '\r', '\n',
            'X', 'Y', '\r', '\n'
        );
        final WithChunks policy = new WithChunks();
        final int[] result = policy.apply(src).toArray();
        assertArrayEquals(new int[] {65, 66}, result);
    }

    public void testInvalidHex() {
        final IntStream src = IntStream.of(
            'G', '\r', '\n',
            'A', 'B', '\r', '\n',
            '0', '\r', '\n'
        );
        final WithChunks policy = new WithChunks();
        try {
            policy.apply(src).forEach(ignored -> {});
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
