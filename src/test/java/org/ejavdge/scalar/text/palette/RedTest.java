package org.ejavdge.scalar.text.palette;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class RedTest extends TestCase {
    public void testColor() throws InvariantViolation {
        assertEquals(
            "\u001B[31mhello\u001B[0m",
            new Red(
                new Text.Of("hello")
            ).content()
        );
    }

    public void testBrokenText() {
        try {
            new Red(
                () -> {
                    throw new InvariantViolation("There is no text");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
