package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class ChildrenOfTest extends TestCase {
    public void testOfRoot() {
        assertEquals(
            "//*/*",
            new ChildrenOf(
                new DocPath.Of("//*")
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