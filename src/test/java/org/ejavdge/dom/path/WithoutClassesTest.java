package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithoutClassesTest extends TestCase {
    public void testSingleClass() {
        assertEquals(
            "//*[not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' main ')"
                + "])]",
            new WithoutClasses(
                new Items.Of<>(new Text.Of("main"))
            ).view()
        );
    }

    public void testMultipleClasses() {
        assertEquals(
            "//*[not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' main ')"
                + "]"
                + " or "
                + "ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' secondary ')"
                + "])]",
            new WithoutClasses(
                new Items.Of<>(
                    new Text.Of("main"),
                    new Text.Of("secondary")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'container'][not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' main ')"
                + "])]",
            new WithoutClasses(
                new Items.Of<>(new Text.Of("main")),
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            new WithoutClasses(new Items.Of<>()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyClassString() {
        assertEquals(
            "//*[not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), '  ')"
                + "])]",
            new WithoutClasses(
                new Items.Of<>(new Text.Of(""))
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithoutClasses(
                new Items.Of<>(new Text.Of("main")),
                () -> {
                    throw new InvariantViolation("path error");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
