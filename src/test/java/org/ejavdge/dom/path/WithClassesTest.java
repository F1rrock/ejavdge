package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithClassesTest extends TestCase {
    public void testSingleClass() {
        assertEquals(
            "//*[@class = 'main']",
            new WithClasses(
                new Items.Of<>(new Text.Of("main"))
            ).view()
        );
    }

    public void testMultipleClasses() {
        assertEquals(
            "//*[@class = 'main' or @class = 'secondary']",
            new WithClasses(
                new Items.Of<>(
                    new Text.Of("main"),
                    new Text.Of("secondary")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'container'][@class = 'main']",
            new WithClasses(
                new Items.Of<>(new Text.Of("main")),
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            new WithClasses(new Items.Of<>()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyClassString() {
        assertEquals(
            "//*[@class = '']",
            new WithClasses(
                new Items.Of<>(new Text.Of(""))
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithClasses(
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
