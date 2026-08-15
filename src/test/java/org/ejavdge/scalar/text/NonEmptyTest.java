package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class NonEmptyTest extends TestCase {
    public void testHello() {
        assertEquals(
            "Hello",
            new NonEmpty(
                new Text.Of("Hello")
            ).content()
        );
    }

    public void testEmpty() {
        try {
            new NonEmpty(new Empty()).content();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
