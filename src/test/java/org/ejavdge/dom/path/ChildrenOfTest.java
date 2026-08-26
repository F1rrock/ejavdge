package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class ChildrenOfTest extends TestCase {
    public void testOfRoot() {
        assertEquals(
            "//*[local-name()='div']//*",
            new ChildrenOf(
                new DocPath.Of("//*[local-name()='div']")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new ChildrenOf(
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