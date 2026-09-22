package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithNameTest extends TestCase {
    public void testNameOnly() {
        assertEquals(
            "//*[@name = 'main']",
            new WithName("main").view()
        );
    }

    public void testNameWithPath() {
        assertEquals(
            "//*[@id = 'container'][@name = 'main']",
            new WithName(
                "main",
                new DocPath.Of("//*[@id = 'container']")
            ).view()
        );
    }

    public void testTextName() {
        assertEquals(
            "//*[@name = 'main']",
            new WithName(new Text.Of("main")).view()
        );
    }

    public void testDelegation() {
        assertEquals(
            "//*",
            new WithName(new DocPath.Of("//*")).view()
        );
    }

    public void testEmptyName() {
        assertEquals(
            "//*[@name = '']",
            new WithName(
                "",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithName(
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
