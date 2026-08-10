package org.ejavdge.web.driver.jdk.socket.body;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public final class ChunkPolicyTest extends TestCase {
    public void testWithChunks() {
        assertArrayEquals(
            new int[] {'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd', '!'},
            new ChunkPolicy(
                """
                Transfer-Encoding: chunked\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> {
                    fail("wrong policy");
                    return IntStream.empty();
                }
            ).apply(
                IntStream.of(
                    '5', '\r', '\n',
                    'H', 'e', 'l', 'l', 'o', '\r', '\n',
                    '6', '\r', '\n',
                    'W', 'o', 'r', 'l', 'd', '!', '\r', '\n',
                    '0', '\r', '\n'
                )
            ).toArray()
        );
    }

    public void testWithoutChunks() {
        assertArrayEquals(
            new int[] {'W', 'o', 'r', 'l', 'd'},
            new ChunkPolicy(
                """
                Content-Length: 0\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> IntStream.of('W', 'o', 'r', 'l', 'd')
            ).apply(
                IntStream.of('H', 'e', 'l', 'l', 'o')
            ).toArray()
        );
    }

    public void testEmptyChunks() {
        assertArrayEquals(
            new int[] {},
            new ChunkPolicy(
                """
                Transfer-Encoding: chunked\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> {
                    fail("wrong policy");
                    return IntStream.empty();
                }
            ).apply(
                IntStream.of('0', '\r', '\n')
            ).toArray()
        );
    }

    public void testOtherTransfer() {
        assertArrayEquals(
            new int[] {'W', 'o', 'r', 'l', 'd'},
            new ChunkPolicy(
                """
                Transfer-Encoding: unknown\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> IntStream.of('W', 'o', 'r', 'l', 'd')
            ).apply(
                IntStream.of('H', 'e', 'l', 'l', 'o')
            ).toArray()
        );
    }
}
