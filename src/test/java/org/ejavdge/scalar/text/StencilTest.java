package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

public final class StencilTest extends TestCase {
    public void testSingleArgument() {
        assertEquals(
            "Hello, world!",
            new Stencil(
                new Text.Of("Hello, %s!"),
                new Text.Of("world")
            ).content()
        );
    }

    public void testSeveralArguments() {
        assertEquals(
            "2 + 3 = 5",
            new Stencil(
                new Text.Of("%s + %s = %s"),
                new Items.Of<>(
                    new Text.Of("2"),
                    new Text.Of("3"),
                    new Text.Of("5")
                )
            ).content()
        );
    }

    public void testWithoutArguments() {
        assertEquals(
            "constant",
            new Stencil(
                new Text.Of("constant")
            ).content()
        );
    }

    public void testMissingArgument() {
        try {
            new Stencil(
                new Text.Of("%s %s"),
                new Text.Of("only one")
            ).content();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }

    public void testInvalidFormat() {
        try {
            new Stencil(
                new Text.Of("%q"),
                new Text.Of("value")
            ).content();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }
}