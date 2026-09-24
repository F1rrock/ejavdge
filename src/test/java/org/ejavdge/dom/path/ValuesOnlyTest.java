package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class ValuesOnlyTest extends TestCase {
    public void testDefaultSeparator() {
        assertEquals(
            "string-join(//*//@value, '\n')",
            new ValuesOnly(
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testCustomSeparator() {
        assertEquals(
            "string-join(//*//@value, ', ')",
            new ValuesOnly(
                new Text.Of(", "),
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new ValuesOnly(
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
            new ValuesOnly(
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
