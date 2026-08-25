package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithoutTagsTest extends TestCase {
    public void testSingleTag() {
        assertEquals(
            "//*[not(local-name() = 'div')]",
            new WithoutTags(
                new Items.Of<>(new Text.Of("div"))
            ).view()
        );
    }

    public void testMultipleTags() {
        assertEquals(
            "//*[not(local-name() = 'div' or local-name() = 'span')]",
            new WithoutTags(
                new Items.Of<>(
                    new Text.Of("div"),
                    new Text.Of("span")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'main'][not(local-name() = 'p')]",
            new WithoutTags(
                new Items.Of<>(new Text.Of("p")),
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            new WithoutTags(new Items.Of<>()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyTagString() {
        try {
            new WithoutTags(
                new Items.Of<>(new Text.Of(""))
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new WithoutTags(
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
