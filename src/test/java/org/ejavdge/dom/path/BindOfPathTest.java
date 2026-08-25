package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class BindOfPathTest extends TestCase {
    public void testBindOfText() {
        assertEquals(
            "hello world",
            new BindOfPath(
                new DocPath.Of("hello"),
                s -> new DocPath.Of(s + " world")
            ).view()
        );
    }

    public void testBindOfEmpty() {
        assertEquals(
            "fallback",
            new BindOfPath(
                new DocPath.Of(""),
                s -> new DocPath.Of("fallback")
            ).view()
        );
    }

    public void testOriginThrows() {
        try {
            new BindOfPath(
                () -> {
                    throw new InvariantViolation("origin error");
                },
                s -> {
                    throw new InvariantViolation("fallback error");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBindingThrows() {
        try {
            new BindOfPath(
                new DocPath.Of("hello"),
                s -> {
                    throw new InvariantViolation("binding error");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
