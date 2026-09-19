package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class WithoutNbspTest extends TestCase {
    public void testWithNbsp() {
        assertEquals(
            "hello world",
            new WithoutNbsp(
                new Text.Of("hello\u00A0world")
            ).content()
        );
    }

    public void testSpaces() {
        assertEquals(
            "hello world",
            new WithoutNbsp(
                new Text.Of("hello world")
            ).content()
        );
    }

    public void testMultipleNbsp() {
        assertEquals(
            "a b c",
            new WithoutNbsp(
                new Text.Of("a\u00A0b\u00A0c")
            ).content()
        );
    }

    public void testOnlyNbsp() {
        assertEquals(
            " ",
            new WithoutNbsp(
                new Text.Of("\u00A0")
            ).content()
        );
    }

    public void testEmptyText() {
        assertEquals(
            "",
            new WithoutNbsp(
                new Text.Of("")
            ).content()
        );
    }

    public void testBrokenOrigin() {
        try {
            new WithoutNbsp(
                () -> {
                    throw new InvariantViolation("origin error");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}