package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithoutClassTest extends TestCase {
    public void testClassOnly() {
        assertEquals(
            "//*[not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' main ')"
                + "])]",
            new WithoutClass("main").view()
        );
    }

    public void testClassWithPath() {
        assertEquals(
            "//*[@id = 'container'][not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' main ')"
                + "])]",
            new WithoutClass(
                "main",
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testTextClass() {
        assertEquals(
            "//*[not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), ' main ')"
                + "])]",
            new WithoutClass(new Text.Of("main")).view()
        );
    }

    public void testDelegation() {
        assertEquals(
            "//*",
            new WithoutClass(new DocPath.Of("//*")).view()
        );
    }

    public void testEmptyClass() {
        assertEquals(
            "//*[not(ancestor-or-self::*["
                + "contains(concat(' ', normalize-space(@class), ' '), '  ')"
                + "])]",
            new WithoutClass(
                "",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithoutClass(
                "main",
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
