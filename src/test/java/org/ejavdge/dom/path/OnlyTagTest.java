package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class OnlyTagTest extends TestCase {
    public void testTagOnly() {
        assertEquals(
            "//*[local-name() = 'div']",
            new OnlyTag("div").view()
        );
    }

    public void testTagWithPath() {
        assertEquals(
            "//*[@id = 'main'][local-name() = 'div']",
            new OnlyTag(
                "div",
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testTextTag() {
        assertEquals(
            "//*[local-name() = 'span']",
            new OnlyTag(new Text.Of("span")).view()
        );
    }

    public void testEmptyTag() {
        try {
            new OnlyTag("").view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new OnlyTag(
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

    public void testDelegation() {
        assertEquals(
            "//*",
            new OnlyTag(new DocPath.Of("//*")).view()
        );
    }
}
