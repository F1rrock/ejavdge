package org.ejavdge.workspace.out;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithoutAnsiTest extends TestCase {
    public void testPlainText() {
        final var builder = new StringBuilder();
        new WithoutAnsi(
            t -> builder.append(t.content())
        ).write(new Text.Of("hello world"));
        assertEquals("hello world", builder.toString());
    }

    public void testRedText() {
        final var builder = new StringBuilder();
        new WithoutAnsi(
            t -> builder.append(t.content())
        ).write(new Text.Of("\u001B[31mhello\u001B[0m"));
        assertEquals("hello", builder.toString());
    }

    public void testAnsiControlSequence() {
        final var builder = new StringBuilder();
        new WithoutAnsi(
            t -> builder.append(t.content())
        ).write(new Text.Of("hello \u001B[2Jworld"));
        assertEquals("hello world", builder.toString());
    }

    public void testBrokenOrigin() {
        try {
            new WithoutAnsi(t -> {
                throw new InvariantViolation("there is no text");
            }).write(new Text.Of("hello world"));
        } catch (final InvariantViolation err) {
            return;
        }
        fail("InvariantViolation");
    }
}
