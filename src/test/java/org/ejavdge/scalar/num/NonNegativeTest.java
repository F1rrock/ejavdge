package org.ejavdge.scalar.num;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class NonNegativeTest extends TestCase {
    public void testValidNum() {
        assertEquals(
            10,
            new NonNegative(
                new Num.Of(10)
            ).value()
        );
    }

    public void testNegative() {
        try {
            new NonNegative(new Num.Of(-10)).value();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }

    public void testZero() {
        assertEquals(
            0,
            new NonNegative(
                new Num.Of(0)
            ).value()
        );
    }
}
