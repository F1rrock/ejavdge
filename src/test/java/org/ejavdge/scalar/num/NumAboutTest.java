package org.ejavdge.scalar.num;

import junit.framework.TestCase;

import org.ejavdge.error.InvariantViolation;

public final class NumAboutTest extends TestCase {
    public void testValue() {
        assertEquals(
            0,
            new NumAbout(
                "zero",
                new Num.Of(0)
            ).value()
        );
    }

    public void testContextInViolation() {
        try {
            new NumAbout(
                "illegal",
                () -> {
                    throw new InvariantViolation("wrong value");
                }
            ).value();
        } catch (final InvariantViolation err) {
            assertEquals(
                "problem with illegal: wrong value",
                err.getMessage()
            );
            return;
        }
        fail("InvariantViolation");
    }

    public void testContextComposition() {
        try {
            new NumAbout(
                "unknown",
                new NumAbout(
                    "illegal",
                    () -> {
                        throw new InvariantViolation("wrong value");
                    }
                )
            ).value();
        } catch (final InvariantViolation err) {
            assertEquals(
                "problem with unknown: problem with illegal: wrong value",
                err.getMessage()
            );
            return;
        }
        fail("InvariantViolation");
    }
}
