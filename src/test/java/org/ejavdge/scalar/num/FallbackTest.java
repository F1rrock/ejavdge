package org.ejavdge.scalar.num;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class FallbackTest extends TestCase {
    public void testPure() {
        assertEquals(
            0,
            new Fallback(
                new Num.Of(0),
                new Num.Of(1)
            ).value()
        );
    }

    public void testFallback() {
        assertEquals(
            -1,
            new Fallback(
                () -> {
                    throw new InvariantViolation("There is no number.");
                },
                new Num.Of(-1)
            ).value()
        );
    }

    public void testFallbackError() {
        try {
            new Fallback(
                () -> {
                    throw new InvariantViolation("There is no number.");
                },
                () -> {
                    throw new InvariantViolation("There is no number.");
                }
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
