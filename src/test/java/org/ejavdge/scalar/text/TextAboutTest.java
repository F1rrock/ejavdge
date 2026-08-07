package org.ejavdge.scalar.text;

import junit.framework.TestCase;

import org.ejavdge.error.InvariantViolation;

public final class TextAboutTest extends TestCase {
    public void testContent() {
        assertEquals(
            "hello",
            new TextAbout(
                "greetings",
                new Text.Of("hello")
            ).content()
        );
    }

    public void testContextInViolation() {
        try {
            new TextAbout(
                "illegal",
                () -> {
                    throw new InvariantViolation("wrong value");
                }
            ).content();
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
            new TextAbout(
                "unknown",
                new TextAbout(
                    "illegal",
                    () -> {
                        throw new InvariantViolation("wrong value");
                    }
                )
            ).content();
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
