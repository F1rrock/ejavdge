package org.ejavdge.scalar.num;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class PositiveTest extends TestCase {
    public void testValidNum() {
        assertEquals(
            10,
            new Positive(
                new Num.Of(10)
            ).value()
        );
    }

    public void testNegative() {
        try {
            new Positive(new Num.Of(-10)).value();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }

    public void testZero() {
        try {
            new Positive(new Num.Of(0)).value();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
