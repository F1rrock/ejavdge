package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class AllOfTest extends TestCase {
    public void testSingle() {
        assertEquals(
            "string-join((a), '\n')",
            new AllOf(
                new DocPath.Of("a")
            ).view()
        );
    }

    public void testMultiple() {
        assertEquals(
            "string-join((a, b), '\n')",
            new AllOf(
                new DocPath.Of("a"),
                new DocPath.Of("b")
            ).view()
        );
    }

    public void testEmpty() {
        assertEquals(
            "string-join((), '\n')",
            new AllOf().view()
        );
    }

    public void testError() {
        try {
            new AllOf(
                () -> {
                    throw new InvariantViolation("there is no xpath");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithCustomSeparator() {
        assertEquals(
            "string-join((a, b), ' ')",
            new AllOf(
                new Text.Of(" "),
                new DocPath.Of("a"),
                new DocPath.Of("b")
            ).view()
        );
    }
}
