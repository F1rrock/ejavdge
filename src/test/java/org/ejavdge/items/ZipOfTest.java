package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ZipOfTest extends TestCase {
    public void testEmptySource() throws InvariantViolation {
        assertEquals(
            List.of(),
            new ZipOf<>(
                new Items.Of<>()
            ).contents()
        );
    }

    public void testSingleList() throws InvariantViolation {
        assertEquals(
            List.of("a", "b", "c"),
            this.flat(
                new ZipOf<>(
                    this.items("a", "b", "c")
                )
            )
        );
    }

    public void testTwoListsOfSameLength() throws InvariantViolation {
        assertEquals(
            List.of("a,1", "b,2", "c,3"),
            this.flat(
                new ZipOf<>(
                    this.items("a", "b", "c"),
                    this.items("1", "2", "3")
                )
            )
        );
    }

    public void testWithLongerFirstList() throws InvariantViolation {
        assertEquals(
            List.of("a,1", "b,2", "c"),
            this.flat(
                new ZipOf<>(
                    this.items("a", "b", "c"),
                    this.items("1", "2")
                )
            )
        );
    }

    public void testWithLongerSecondList() throws InvariantViolation {
        assertEquals(
            List.of("a,1", "2", "3"),
            this.flat(
                new ZipOf<>(
                    this.items("a"),
                    this.items("1", "2", "3")
                )
            )
        );
    }

    public void testThreeListsOfSameLength() throws InvariantViolation {
        assertEquals(
            List.of("a,1,x", "b,2,y"),
            this.flat(
                new ZipOf<>(
                    this.items("a", "b"),
                    this.items("1", "2"),
                    this.items("x", "y")
                )
            )
        );
    }

    public void testThreeListsOfDifferentLengths() throws InvariantViolation {
        assertEquals(
            List.of("a,1,x", "b,2,y", "c", "d"),
            this.flat(
                new ZipOf<>(
                    this.items("a", "b", "c", "d"),
                    this.items("1", "2"),
                    this.items("x", "y")
                )
            )
        );
    }

    public void testEmptyInnerList() throws InvariantViolation {
        assertEquals(
            List.of("a", "b", "c"),
            this.flat(
                new ZipOf<>(
                    this.items("a", "b", "c"),
                    new Items.Of<>()
                )
            )
        );
    }

    public void testEmptyLists() throws InvariantViolation {
        assertEquals(
            List.of(),
            new ZipOf<>(
                new Items.Of<>(),
                new Items.Of<>()
            ).contents()
        );
    }

    public void testBrokenSource() {
        try {
            new ZipOf<>(
                () -> {
                    throw new InvariantViolation(
                        "There is no source."
                    );
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithBrokenList() {
        try {
            new ZipOf<>(
                this.items("a", "b"),
                () -> {
                    throw new InvariantViolation(
                        "There is no list."
                    );
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private Items<Text> items(final String ...xs) {
        return new Items.Of<>(
            Arrays.stream(xs)
                .map(Text.Of::new)
                .toArray(Text[]::new)
        );
    }

    private List<String> flat(final Items<Items<Text>> zip)
            throws InvariantViolation {
        return zip.contents()
            .stream()
            .map(this::joined)
            .toList();
    }

    private String joined(final Items<Text> tuple)
            throws InvariantViolation {
        return tuple.contents()
            .stream()
            .map(Text::content)
            .collect(Collectors.joining(","));
    }
}
