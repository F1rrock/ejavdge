package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class BeforeTagsTest extends TestCase {
    public void testSingleTag() {
        assertEquals(
            "//*[following-sibling::*["
                + "local-name() = 'div'"
                + "]"
                + " and "
                + "not(preceding-sibling::*["
                + "local-name() = 'div'"
                + "])"
                + " and "
                + "not("
                + "local-name() = 'div'"
                + ")]",
            new BeforeTags(
                new Items.Of<>(new Text.Of("div"))
            ).view()
        );
    }

    public void testMultipleTags() {
        assertEquals(
            "//*[following-sibling::*["
                + "local-name() = 'div'"
                + " or "
                + "local-name() = 'span'"
                + "]"
                + " and "
                + "not(preceding-sibling::*["
                + "local-name() = 'div'"
                + " or "
                + "local-name() = 'span'"
                + "])"
                + " and "
                + "not("
                + "local-name() = 'div'"
                + " or "
                + "local-name() = 'span'"
                + ")]",
            new BeforeTags(
                new Items.Of<>(
                    new Text.Of("div"),
                    new Text.Of("span")
                )
            ).view()
        );
    }

    public void testWithPath() {
        assertEquals(
            "//*[@id = 'container'][following-sibling::*["
                + "local-name() = 'div'"
                + "]"
                + " and "
                + "not(preceding-sibling::*["
                + "local-name() = 'div'"
                + "])"
                + " and "
                + "not("
                + "local-name() = 'div'"
                + ")]",
            new BeforeTags(
                new Items.Of<>(new Text.Of("div")),
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testEmptyItems() {
        try {
            new BeforeTags(new Items.Of<>()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyTagString() {
        try {
            new BeforeTags(
                new Items.Of<>(new Text.Of(""))
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new BeforeTags(
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
