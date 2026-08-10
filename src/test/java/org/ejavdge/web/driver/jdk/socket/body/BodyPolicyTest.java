package org.ejavdge.web.driver.jdk.socket.body;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public final class BodyPolicyTest extends TestCase {
    public void testContentLength() {
        final byte[] headers = "Content-Length: 5\r\n".getBytes(StandardCharsets.UTF_8);
        final IntStream data = IntStream.of(
    'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'
        );
        final BodyPolicy policy = new BodyPolicy(headers);
        final int[] result = policy.apply(data).toArray();
        assertArrayEquals(new int[]{'H', 'e', 'l', 'l', 'o'}, result);
    }

    public void testChunked() {
        final byte[] headers = "Transfer-Encoding: chunked\r\n".getBytes(StandardCharsets.UTF_8);
        final IntStream data = IntStream.of(
            '5', '\r', '\n',
            'H', 'e', 'l', 'l', 'o', '\r', '\n',
            '0', '\r', '\n'
        );
        final BodyPolicy policy = new BodyPolicy(headers);
        final int[] result = policy.apply(data).toArray();
        assertArrayEquals(new int[]{'H', 'e', 'l', 'l', 'o'}, result);
    }

    public void testUnsupported() {
        final byte[] headers = "Server: nginx\r\n".getBytes(StandardCharsets.UTF_8);
        final IntStream data = IntStream.of(
    'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'
        );
        final BodyPolicy policy = new BodyPolicy(headers);
        try {
            policy.apply(data).forEach(ignored -> {});
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testPriority() {
        final byte[] headers = """
            Content-Length: 3
            Transfer-Encoding: chunked
            """.getBytes(StandardCharsets.UTF_8);
        final IntStream data = IntStream.of(
    'A', 'B', 'C', 'D', 'E'
        );
        final BodyPolicy policy = new BodyPolicy(headers);
        final int[] result = policy.apply(data).toArray();
        assertArrayEquals(new int[]{'A', 'B', 'C'}, result);
    }

    public void testEmptyData() {
        final byte[] headers = "Content-Length: 0\r\n".getBytes(StandardCharsets.UTF_8);
        final IntStream data = IntStream.of('H', 'e', 'l', 'l', 'o');
        final BodyPolicy policy = new BodyPolicy(headers);
        final int[] result = policy.apply(data).toArray();
        assertEquals(0, result.length);
    }
}