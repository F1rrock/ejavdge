package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class BeforeIdTest extends TestCase {
    public void testWithStringId() {
        assertEquals(
            "//*[following-sibling::*[@id = 'target']]",
            new BeforeId(
                "target",
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testWithTextId() {
        assertEquals(
            "//*[following-sibling::*[@id = 'target']]",
            new BeforeId(
                new Text.Of("target"),
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testEmptyId() {
        try {
            new BeforeId(
                new Text.Of(""),
                new DocPath.Of("//*")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPath() {
        try {
            new BeforeId(
                "target",
                () -> {
                    throw new InvariantViolation("path error");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
