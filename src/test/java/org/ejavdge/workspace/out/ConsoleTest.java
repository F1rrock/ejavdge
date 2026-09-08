package org.ejavdge.workspace.out;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public final class ConsoleTest extends TestCase {
    public void testWriting() throws InvariantViolation {
        final var output = new ByteArrayOutputStream();
        new Console(
            new PrintStream(
                output
            )
        ).write(new Text.Of("hello"));
        assertEquals(
            "hello",
            output.toString()
        );
    }

    public void testBrokenMessage() {
        try {
            new Console(
                new PrintStream(
                    new ByteArrayOutputStream()
                )
            ).write(() -> {
                throw new InvariantViolation("There is no text");
            });
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
