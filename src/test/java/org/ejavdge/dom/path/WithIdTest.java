package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithIdTest extends TestCase {
    public void testWithStringId() {
        assertEquals(
            "//*[@id = 'main']",
            new WithId(
                "main",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testWithTextId() {
        assertEquals(
            "//*[@id = 'main']",
            new WithId(
                new Text.Of("main"),
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testEmptyId() {
        assertEquals(
            "//*[@id = '']",
            new WithId(
                "",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithId(
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
