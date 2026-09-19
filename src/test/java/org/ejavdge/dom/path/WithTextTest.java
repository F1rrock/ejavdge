package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class WithTextTest extends TestCase {
    public void testNonEmptyText() {
        assertEquals(
            "//*[text() = 'local-test']",
            new WithText(
                "local-test",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testEmptyText() {
        assertEquals(
            "//*[text() = '']",
            new WithText(
                "",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testWithNonRoot() {
        assertEquals(
            "//*[@id = 'main'][text() = 'local-test']",
            new WithText(
                "local-test",
                new DocPath.Of("//*[@id = 'main']")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new WithText(
                "local-test",
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
