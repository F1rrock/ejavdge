package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Empty;

public final class BeforeTagTest extends TestCase {
    public void testDivTag() {
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
            new BeforeTag("div").view()
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
            new BeforeTag(
                "div",
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testEmptyTag() {
        try {
            new BeforeTag(new Empty()).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new BeforeTag(
                "div",
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
