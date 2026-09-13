package org.ejavdge.workspace.out;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonAnsiText;
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
        assertEquals("hello world", builder.toString());
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
