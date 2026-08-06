package org.ejavdge.web.driver.jdk.stream;

import junit.framework.TestCase;

import java.util.List;
import java.util.stream.Stream;

public final class TeeTest extends TestCase {
    public void testBroadcastToLeftAndRight() {
        final var tee = new Tee<>(
            Stream.of(1, 2, 3, 4)
        );
        assertEquals(
            List.of(1, 2, 3, 4),
            tee.left().toList()
        );
        assertEquals(
            List.of(1, 2, 3, 4),
            tee.right().toList()
        );
    }

    public void testBroadcastToRightAndLeft() {
        final var tee = new Tee<>(
            Stream.of(1, 2, 3, 4)
        );
        assertEquals(
            List.of(1, 2, 3, 4),
            tee.right().toList()
        );
        assertEquals(
            List.of(1, 2, 3, 4),
            tee.left().toList()
        );
    }

    public void testPreservesUnreadPartForOtherSide() {
        final var tee = new Tee<>(
            Stream.of(1, 2, 3, 4)
        );
        assertEquals(
            List.of(1, 2),
            tee.left().limit(2).toList()
        );
        assertEquals(
            List.of(1, 2, 3, 4),
            tee.right().toList()
        );
    }

    public void testPreservesUnreadPartWhenRightReadsFirst() {
        final var tee = new Tee<>(
            Stream.of(1, 2, 3, 4)
        );
        assertEquals(
            List.of(1, 2),
            tee.right().limit(2).toList()
        );
        assertEquals(
            List.of(1, 2, 3, 4),
            tee.left().toList()
        );
    }

    public void testSplitsIndependentConsumers() {
        final var tee = new Tee<>(
            Stream.of(1, 2, 3, 4, 5)
        );
        final var left = tee.left();
        final var right = tee.right();
        assertEquals(
            List.of(1, 2),
            left.limit(2).toList()
        );
        assertEquals(
            List.of(1, 2, 3, 4, 5),
            right.toList()
        );
    }
}
