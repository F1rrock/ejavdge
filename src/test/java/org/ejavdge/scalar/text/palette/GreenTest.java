package org.ejavdge.scalar.text.palette;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class GreenTest extends TestCase {
    public void testColor() throws InvariantViolation {
        assertEquals(
            "\u001B[32mhello\u001B[0m",
            new Green(
                new Text.Of("hello")
            ).content()
        );
    }

    public void testBrokenText() {
        try {
            new Green(
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
