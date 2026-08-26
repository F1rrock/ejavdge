package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithClassTest extends TestCase {
    public void testClassOnly() {
        assertEquals(
            "//*[@class = 'main']",
            new WithClass("main").view()
        );
    }

    public void testClassWithPath() {
        assertEquals(
            "//*[@id = 'container'][@class = 'main']",
            new WithClass(
                "main",
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testTextClass() {
        assertEquals(
            "//*[@class = 'main']",
            new WithClass(new Text.Of("main")).view()
        );
    }

    public void testDelegation() {
        assertEquals(
            "//*",
            new WithClass(new DocPath.Of("//*")).view()
        );
    }

    public void testEmptyClass() {
        assertEquals(
            "//*[@class = '']",
            new WithClass(
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
