package org.ejavdge.items;

import junit.framework.TestCase;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class ItemsAboutTest extends TestCase {
    public void testContents() {
        assertEquals(
            List.of(1, 2, 3),
            new ItemsAbout<>(
                "greetings",
                new Items.Of<>(1, 2, 3)
            ).contents()
        );
    }

    public void testContextInViolation() {
        try {
            new ItemsAbout<>(
                "illegal",
                () -> {
                    throw new InvariantViolation("wrong value");
                }
            ).contents();
        } catch (final InvariantViolation err) {
            assertEquals(
                "problem with illegal: wrong value",
                err.getMessage()
            );
            return;
        }
        fail("InvariantViolation");
    }

    public void testContextComposition() {
        try {
            new ItemsAbout<>(
                "unknown",
                new ItemsAbout<>(
                    "illegal",
                    () -> {
                        throw new InvariantViolation("wrong value");
                    }
                )
            ).contents();
        } catch (final InvariantViolation err) {
            assertEquals(
                "problem with unknown: problem with illegal: wrong value",
                err.getMessage()
            );
            return;
        }
        fail("InvariantViolation");
    }
}
