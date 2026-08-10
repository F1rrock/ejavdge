package org.ejavdge.web.driver.jdk.socket.body;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;

import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

public final class WithLimitationTest extends TestCase {
    public void testLimit() {
        final IntStream src = IntStream.range(0, 10);
        final WithLimitation policy = new WithLimitation(new Num.Of(5));
        final int[] result = policy.apply(src).toArray();
        assertArrayEquals(new int[] {0, 1, 2, 3, 4}, result);
    }

    public void testLimitZero() {
        final IntStream src = IntStream.range(0, 10);
        final WithLimitation policy = new WithLimitation(new Num.Of(0));
        final int[] result = policy.apply(src).toArray();
        assertEquals(0, result.length);
    }

    public void testLimitGreaterThanSize() {
        final IntStream src = IntStream.range(0, 3);
        final WithLimitation policy = new WithLimitation(new Num.Of(10));
        final int[] result = policy.apply(src).toArray();
        assertArrayEquals(new int[] {0, 1, 2}, result);
    }
}
