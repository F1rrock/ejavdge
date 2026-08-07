package org.ejavdge.scalar.text;

import junit.framework.TestCase;

import org.ejavdge.error.InvariantViolation;

public final class ContextualTest extends TestCase {

    public void testKeepsOriginalContent() {
        assertEquals(
            "hello",
            new Contextual(
                "message",
                new Text.Of("hello")
            ).content()
        );
    }

    public void testAddsContextToViolation() {
        try {
            new Contextual(
                "Illegal value",
                new Text() {
                    @Override
                    public String content() throws InvariantViolation {
                        throw new InvariantViolation(
                            "Wrong value"
                        );
                    }
                }
            ).content();
        } catch (final InvariantViolation err) {
            assertEquals(
                "Illegal value: Wrong value",
                err.getMessage()
            );
            return;
        }
        fail("InvariantViolation expected");
    }
}