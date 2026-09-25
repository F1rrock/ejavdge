package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class MatchTest extends TestCase {
    public void testMatch() {
        assertEquals(
            "123",
            new Match(
                new Text.Of("foo123bar"),
                new Text.Of("\\d+")
            ).content()
        );
    }

    public void testFirstMatch() {
        assertEquals(
            "123",
            new Match(
                new Text.Of("foo123bar456"),
                new Text.Of("\\d+")
            ).content()
        );
    }

    public void testNoMatch() {
        try {
            new Match(
                new Text.Of("foo"),
                new Text.Of("\\d+")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithCustomMessage() {
        try {
            new Match(
                new Text.Of("foo"),
                new Text.Of("\\d+"),
                new Text.Of("There is no valid text.")
            ).content();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals("There is no valid text.", e.getMessage());
        }
    }
}
