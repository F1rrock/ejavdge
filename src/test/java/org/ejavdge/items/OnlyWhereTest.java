package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

public final class OnlyWhereTest extends TestCase {
    private static final Predicate<Integer> IS_ZERO = x -> x == 0;

    public void testEmptySource() {
        assertEquals(
            List.of(),
            new OnlyWhere<>(IS_ZERO, new Items.Of<>()).contents()
        );
    }

    public void testAllMatch() {
        assertEquals(
            List.of(0, 0, 0),
            new OnlyWhere<>(IS_ZERO, new Items.Of<>(0, 0, 0)).contents()
        );
    }

    public void testNoneMatch() {
        assertEquals(
            List.of(),
            new OnlyWhere<>(IS_ZERO, new Items.Of<>(1, 2, 3)).contents()
        );
    }

    public void testSomeMatch() {
        assertEquals(
            List.of(0, 0),
            new OnlyWhere<>(IS_ZERO, new Items.Of<>(1, 0, 2, 0, 3)).contents()
        );
    }

    public void testOnlyFirstMatches() {
        assertEquals(
            List.of(0),
            new OnlyWhere<>(IS_ZERO, new Items.Of<>(0, 1, 2)).contents()
        );
    }

    public void testOnlyLastMatches() {
        assertEquals(
            List.of(0),
            new OnlyWhere<>(IS_ZERO, new Items.Of<>(1, 2, 0)).contents()
        );
    }

    public void testBrokenSource() {
        try {
            new OnlyWhere<>(
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
