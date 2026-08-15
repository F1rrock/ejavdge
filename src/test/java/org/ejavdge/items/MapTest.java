package org.ejavdge.items;

import junit.framework.TestCase;

import java.util.function.Function;

public final class MapTest extends TestCase {
    public void testIdentity() {
        final Items<Integer> xs = new Items.Of<>(1, 2, 3);
        assertEquals(
            xs.contents(),
            new Map<>(
                Function.identity(),
                xs
            ).contents()
        );
    }

    public void testComposition() {
        final Items<Integer> xs = new Items.Of<>(1, 2, 3);
        final Function<Integer, Integer> f = x -> x + 1;
        final Function<Integer, Integer> g = x -> x * 2;
        assertEquals(
            new Map<>(
                g.compose(f),
                xs
            ).contents(),
            new Map<>(
                g,
                new Map<>(
                        f,
                        xs
                )
            ).contents()
        );
    }
}
