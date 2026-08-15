package org.ejavdge.items;

import junit.framework.TestCase;

import java.util.List;

public final class JointTest extends TestCase {
    public void testSingle() {
        assertEquals(
            List.of(1, 2, 3),
            new Joint<>(
                new Items.Of<>(1, 2, 3)
            ).contents()
        );
    }

    public void testSeveralItems() {
        assertEquals(
            List.of(1, 2, 3, 4, 5),
            new Joint<>(
                new Items.Of<>(1),
                new Items.Of<>(2, 3),
                new Items.Of<>(4),
                new Items.Of<>(5)
            ).contents()
        );
    }

    public void testEmpty() {
        assertEquals(
            List.of(),
            new Joint<Integer>()
                .contents()
        );
    }
}
