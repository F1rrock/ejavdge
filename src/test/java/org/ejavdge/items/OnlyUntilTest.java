package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

public final class OnlyUntilTest extends TestCase {
    private static final Predicate<Integer> IS_ZERO = x -> x == 0;

    public void testEmptySource() {
        assertEquals(
            List.of(),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>()).contents()
        );
    }

    public void testWithoutSeparator() {
        assertEquals(
            List.of(1, 2, 3),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>(1, 2, 3)).contents()
        );
    }

    public void testSeparatorInMiddle() {
        assertEquals(
            List.of(1, 2),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>(1, 2, 0, 3, 4)).contents()
        );
    }

    public void testSeparatorAtStart() {
        assertEquals(
            List.of(),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>(0, 1, 2)).contents()
        );
    }

    public void testSeparatorAtEnd() {
        assertEquals(
            List.of(1, 2, 3),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>(1, 2, 3, 0)).contents()
        );
    }

    public void testConsecutiveSeparators() {
        assertEquals(
            List.of(1),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>(1, 0, 0, 2)).contents()
        );
    }

    public void testSeparatorOnly() {
        assertEquals(
            List.of(),
            new OnlyUntil<>(IS_ZERO, new Items.Of<>(0)).contents()
        );
    }

    public void testBrokenSource() {
        try {
            new OnlyUntil<>(
                IS_ZERO,
                () -> {
                    throw new InvariantViolation("source error");
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
