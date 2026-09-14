package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class NonAnsiTextTest extends TestCase {
    public void testPlainText() {
        assertEquals(
            "hello world",
            new NonAnsiText(
                new Text.Of("hello world")
            ).content()
        );
    }

    public void testRedText() {
        assertEquals(
            "hello",
            new NonAnsiText(
                new Text.Of("\u001B[31mhello\u001B[0m")
            ).content()
        );
    }

    public void testAnsiControlSequence() {
        assertEquals(
            "hello world",
            new NonAnsiText(
                new Text.Of("hello \u001B[2Jworld")
            ).content()
        );
    }

    public void testBrokenOrigin() {
        try {
            new NonAnsiText(() -> {
                throw new InvariantViolation("there is no text");
            }).content();
        } catch (final InvariantViolation err) {
            return;
        }
        fail("InvariantViolation");
    }
}
