package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class BindOfTextTest extends TestCase {
    public void testBindOfText() {
        assertEquals(
            "hello world",
            new BindOfText(
                new Text.Of("hello"),
                s -> new Text.Of(s + " world")
            ).content()
        );
    }

    public void testBindOfEmpty() {
        assertEquals(
            "fallback",
            new BindOfText(
                new Empty(),
                s -> new Text.Of("fallback")
            ).content()
        );
    }

    public void testOriginThrows() {
        try {
            new BindOfText(
                () -> {
                    throw new InvariantViolation("origin error");
                },
                s -> {
                    throw new InvariantViolation("fallback error");
                }
            ).content();
            fail("InvariantViolation expected");
        } catch (final InvariantViolation e) {
            assertEquals("origin error", e.getMessage());
        }
    }

    public void testBindingThrows() {
        try {
            new BindOfText(
                new Text.Of("hello"),
                s -> {
                    throw new InvariantViolation("binding error");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
