package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class NestedTagsTest extends TestCase {
    public void testItem() {
        assertEquals(
            "//*[local-name() = 'div']//*[local-name() = 'a']",
            new NestedTags(
                new Items.Of<>(new Text.Of("a")),
                new DocPath.Of("//*[local-name() = 'div']")
            ).view()
        );
    }

    public void testItems() {
        assertEquals(
            "//*[local-name() = 'div']//*[local-name() = 'a' or local-name() = 'span']",
            new NestedTags(
                new Items.Of<>(
                    new Text.Of("a"),
                    new Text.Of("span")
                ),
                new DocPath.Of("//*[local-name() = 'div']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            System.out.println(
                new NestedTags(
                    new Items.Of<>(),
                    new DocPath.Of("//*[local-name() = 'div']")
                ).view()
            );
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyTag() {
        try {
            new NestedTags(
                new Items.Of<>(new Text.Of("")),
                new DocPath.Of("\"//*[local-name() = 'div']\"")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new NestedTags(
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
