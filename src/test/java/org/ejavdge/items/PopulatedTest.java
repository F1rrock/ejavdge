package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class PopulatedTest extends TestCase {
    public void testWithNonEmpty() {
        assertEquals(
            List.of(1, 2, 3),
            new Populated<>(
                new Items.Of<>(1, 2, 3)
            ).contents()
        );
    }

    public void testWithEmpty() {
        try {
            new Populated<>(
                new Items.Of<>()
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenOrigin() {
        try {
            new Populated<>(
                () -> {
                    throw new InvariantViolation("origin error");
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
