package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;

import java.util.List;

public final class OnlyFirstTest extends TestCase {
    private final Items<Text> empty = new Items.Of<>();
    private final Items<Text> single = new Items.Of<>(
        new Text.Of("1")
    );
    private final Items<Text> several = new Items.Of<>(
        new Text.Of("1"),
        new Text.Of("2"),
        new Text.Of("3")
    );

    public void testEmptySource() {
        assertEquals(
            List.of(),
            new OnlyFirst<>(this.empty).contents()
        );
    }

    public void testSingleElementSource() {
        assertEquals(
            List.of("1"),
            new OnlyFirst<>(this.single)
                .contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testSeveralElementSource() {
        assertEquals(
            List.of("1"),
            new OnlyFirst<>(this.several)
                .contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testOnlyFirstN() {
        assertEquals(
            List.of("1", "2"),
            new OnlyFirst<>(new Num.Of(2), this.several)
                .contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testOnlyZeroElements() {
        assertEquals(
            List.of(),
            new OnlyFirst<>(new Num.Of(0), this.several).contents()
        );
    }

    public void testOnlyFirstMoreThanSizeElements() {
        assertEquals(
            List.of("1", "2", "3"),
            new OnlyFirst<>(new Num.Of(10), this.several)
                .contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testOnlyNegativeAmountOfElements() {
        try {
            new OnlyFirst<>(new Num.Of(-1), this.several)
                .contents()
                .stream()
                .map(Text::content)
                .forEach(ignored -> {});
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenSource() {
        try {
            new OnlyFirst<Text>(
                () -> {
                    throw new InvariantViolation("There is no items.");
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
