package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

public final class SplitTest extends TestCase {
    private static final Predicate<Integer> IS_ZERO = x -> x == 0;

    public void testEmptySource() {
        assertEquals(
            List.of(),
            new Split<>(IS_ZERO, new Items.Of<>())
                .contents()
        );
    }

    public void testWithoutSeparator() {
        assertEquals(
            List.of(List.of(1, 2, 3)),
            new Split<>(IS_ZERO, new Items.Of<>(1, 2, 3))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testSingleSeparator() {
        assertEquals(
            List.of(List.of(1), List.of(2, 3)),
            new Split<>(IS_ZERO, new Items.Of<>(1, 0, 2, 3))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testSeparatorAtStart() {
        assertEquals(
            List.of(List.of(), List.of(1, 2)),
            new Split<>(IS_ZERO, new Items.Of<>(0, 1, 2))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testTrailingSeparator() {
        assertEquals(
            List.of(List.of(1, 2)),
            new Split<>(IS_ZERO, new Items.Of<>(1, 2, 0))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testConsecutiveSeparators() {
        assertEquals(
            List.of(List.of(1), List.of(), List.of(2)),
            new Split<>(IS_ZERO, new Items.Of<>(1, 0, 0, 2))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testSeparatorOnly() {
        assertEquals(
            List.of(List.of()),
            new Split<>(IS_ZERO, new Items.Of<>(0))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testAllSeparators() {
        assertEquals(
            List.of(List.of(), List.of()),
            new Split<>(IS_ZERO, new Items.Of<>(0, 0))
                .contents()
                .stream()
                .map(Items::contents)
                .toList()
        );
    }

    public void testBrokenSource() {
        try {
            new Split<>(
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
