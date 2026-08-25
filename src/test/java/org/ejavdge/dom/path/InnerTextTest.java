package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class InnerTextTest extends TestCase {
    public void testDefaultSeparator() {
        assertEquals(
            "string-join(//*/text(), '\n')",
            new InnerText(
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testCustomSeparator() {
        assertEquals(
            "string-join(//*/text(), ', ')",
            new InnerText(
                new Text.Of(", "),
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new InnerText(
                () -> {
                    throw new InvariantViolation("path error");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenSeparator() {
        try {
            new InnerText(
                () -> {
                    throw new InvariantViolation("separator error");
                },
                new DocPath.Of("//*")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
