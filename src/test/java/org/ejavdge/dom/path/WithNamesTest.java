package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithNamesTest extends TestCase {
    public void testSingleName() {
        assertEquals(
            "//*[@name = 'main']",
            new WithNames(
                new Items.Of<>(new Text.Of("main"))
            ).view()
        );
    }

    public void testMultipleNames() {
        assertEquals(
            "//*[@name = 'main' or @name = 'secondary']",
            new WithNames(
                new Items.Of<>(
                    new Text.Of("main"),
                    new Text.Of("secondary")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'container'][@name = 'main']",
            new WithNames(
                new Items.Of<>(new Text.Of("main")),
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            new WithNames(new Items.Of<>()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyNameString() {
        assertEquals(
            "//*[@name = '']",
            new WithNames(
                new Items.Of<>(new Text.Of(""))
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithNames(
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
