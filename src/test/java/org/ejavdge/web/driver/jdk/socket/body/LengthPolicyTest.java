package org.ejavdge.web.driver.jdk.socket.body;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public final class LengthPolicyTest extends TestCase {
    public void testWithContentLength() {
        assertArrayEquals(
            new int[] {'H', 'e', 'l', 'l'},
            new LengthPolicy(
                """
                Content-Length: 4\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> {
                    fail("wrong policy");
                    return IntStream.empty();
                }
            ).apply(
                IntStream.of('H', 'e', 'l', 'l', 'o')
            ).toArray()
        );
    }

    public void testWithoutContentLength() {
        assertArrayEquals(
            new int[] {'W', 'o', 'r', 'l', 'd'},
            new LengthPolicy(
                """
                Transfer-Encoding: chunked\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> IntStream.of('W', 'o', 'r', 'l', 'd')
            ).apply(
                IntStream.of('H', 'e', 'l', 'l', 'o')
            ).toArray()
        );
    }

    public void testZeroContentLength() {
        assertArrayEquals(
            new int[] {},
            new LengthPolicy(
                """
                Content-Length: 0\r
                """.getBytes(StandardCharsets.UTF_8),
                ignored -> {
                    fail("wrong policy");
                    return IntStream.empty();
                }
            ).apply(
                IntStream.of('H', 'e', 'l', 'l', 'o')
            ).toArray()
        );
    }
}
