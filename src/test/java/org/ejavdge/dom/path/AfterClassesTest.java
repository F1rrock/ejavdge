package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class AfterClassesTest extends TestCase {
    public void testSingleClass() {
        assertEquals(
            "//*[preceding-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + "]"
                + " and "
                + "not(following-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + "])"
                + " and "
                + "not("
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + ")]",
            new AfterClasses(
                new Items.Of<>(new Text.Of("table"))
            ).view()
        );
    }

    public void testMultipleClasses() {
        assertEquals(
            "//*[preceding-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + " or "
                + "contains(concat(' ', normalize-space(@class), ' '), ' b1 ')"
                + "]"
                + " and "
                + "not(following-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + " or "
                + "contains(concat(' ', normalize-space(@class), ' '), ' b1 ')"
                + "])"
                + " and "
                + "not("
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + " or "
                + "contains(concat(' ', normalize-space(@class), ' '), ' b1 ')"
                + ")]",
            new AfterClasses(
                new Items.Of<>(
                    new Text.Of("table"),
                    new Text.Of("b1")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'container'][preceding-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + "]"
                + " and "
                + "not(following-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + "])"
                + " and "
                + "not("
                + "contains(concat(' ', normalize-space(@class), ' '), ' table ')"
                + ")]",
            new AfterClasses(
                new Items.Of<>(new Text.Of("table")),
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            new AfterClasses(new Items.Of<>()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyClassString() {
        assertEquals(
            "//*[preceding-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), '  ')"
                + "]"
                + " and "
                + "not(following-sibling::*["
                + "contains(concat(' ', normalize-space(@class), ' '), '  ')"
                + "])"
                + " and "
                + "not("
                + "contains(concat(' ', normalize-space(@class), ' '), '  ')"
                + ")]",
            new AfterClasses(
                new Items.Of<>(new Text.Of(""))
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new AfterClasses(
                new Items.Of<>(new Text.Of("table")),
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
