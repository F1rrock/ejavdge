package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithoutTagTest extends TestCase {
    public void testTagOnly() {
        assertEquals(
            "//*[not(local-name() = 'div')]",
            new WithoutTag("div").view()
        );
    }

    public void testTagWithPath() {
        assertEquals(
            "//*[@id = 'main'][not(local-name() = 'div')]",
            new WithoutTag(
                "div",
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testTextTag() {
        assertEquals(
            "//*[not(local-name() = 'span')]",
            new WithoutTag(new Text.Of("span")).view()
        );
    }

    public void testDelegation() {
        assertEquals(
            "//*",
            new WithoutTag(new DocPath.Of("//*")).view()
        );
    }

    public void testEmptyTag() {
        try {
            new WithoutTag("").view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new WithoutTag(
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
