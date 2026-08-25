package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithoutClassesTest extends TestCase {
    public void testSingleClass() {
        assertEquals(
            "//*[not(@class = 'main')]",
            new WithoutClasses(
                new Items.Of<>(new Text.Of("main"))
            ).view()
        );
    }

    public void testMultipleClasses() {
        assertEquals(
            "//*[not(@class = 'main' or @class = 'secondary')]",
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
            "//*[@id = 'container'][not(@class = 'main')]",
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
            "//*[not(@class = '')]",
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
