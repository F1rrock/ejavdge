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
                "problem with illegal\nwrong value",
                err.getMessage() + err.getCause().getMessage()
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
                "problem with unknown\nproblem with illegal\nwrong value",
                err.getMessage() + err.getCause().getMessage()
                    + err.getCause().getCause().getMessage()
            );
            return;
        }
        fail("InvariantViolation");
    }
}
