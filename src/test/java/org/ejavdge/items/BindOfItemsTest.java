package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class BindOfItemsTest extends TestCase {
    public void testBindOfItems() {
        assertEquals(
            List.of(2, 3, 4),
            new BindOfItems<>(
                new Items.Of<>(1, 2, 3),
                xs -> new Items.Of<>(xs.stream().map(x -> x + 1).toList())
            ).contents()
        );
    }

    public void testBindOfEmpty() {
        assertEquals(
            List.of("fallback"),
            new BindOfItems<>(
                new Items.Of<>(),
                xs -> new Items.Of<>("fallback")
            ).contents()
        );
    }

    public void testOriginThrows() {
        try {
            new BindOfItems<>(
                () -> {
                    throw new InvariantViolation("origin error");
                },
                xs -> {
                    throw new InvariantViolation("fallback error");
                }
            ).contents();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals("origin error", e.getMessage());
        }
    }

    public void testBindingThrows() {
        try {
            new BindOfItems<>(
                new Items.Of<>("hello"),
                xs -> {
                    throw new InvariantViolation("binding error");
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
