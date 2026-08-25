package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class OnlyTagsTest extends TestCase {
    public void testStringVararg() {
        assertEquals(
            "//*[local-name() = 'div']",
            new OnlyTags("div").view()
        );
    }

    public void testStringVarargs() {
        assertEquals(
            "//*[local-name() = 'div' or local-name() = 'span']",
            new OnlyTags("div", "span").view()
        );
    }

    public void testTextVararg() {
        assertEquals(
            "//*[local-name() = 'div']",
            new OnlyTags(new Text.Of("div")).view()
        );
    }

    public void testTextVarargs() {
        assertEquals(
            "//*[local-name() = 'div' or local-name() = 'span']",
            new OnlyTags(
                new Text.Of("div"),
                new Text.Of("span")
            ).view()
        );
    }

    public void testItem() {
        assertEquals(
            "//*[local-name() = 'div']",
            new OnlyTags(
                new Items.Of<>(new Text.Of("div"))
            ).view()
        );
    }

    public void testItems() {
        assertEquals(
            "//*[local-name() = 'div' or local-name() = 'span']",
            new OnlyTags(
                new Items.Of<>(
                    new Text.Of("div"),
                    new Text.Of("span")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'main'][local-name() = 'p']",
            new OnlyTags(
                new Items.Of<>(new Text.Of("p")),
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            System.out.println(
                new OnlyTags(new Items.Of<>()).view()
            );
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyTag() {
        try {
            new OnlyTags("").view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new OnlyTags(
                new Items.Of<>(new Text.Of("div")),
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
