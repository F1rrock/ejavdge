package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class NestedTagTest extends TestCase {
    public void testStringTag() {
        assertEquals(
            "//*[@id = 'main']//*[local-name() = 'div']",
            new NestedTag(
                "div",
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testTextTag() {
        assertEquals(
            "//*[@id = 'main']//*[local-name() = 'span']",
            new NestedTag(
                new Text.Of("span"),
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testEmptyTag() {
        try {
            new NestedTag(
                "",
                new DocPath.Of("//*[@id = 'main']")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new NestedTag(
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
            new NestedTag(new DocPath.Of("//*")).view()
        );
    }
}
