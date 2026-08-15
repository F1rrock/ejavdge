package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class FallbackTest extends TestCase {
    public void testPure() {
        assertEquals(
            "HelloWorld",
            new Fallback(
                new Text.Of("HelloWorld"),
                new Text.Of("Wrong")
            ).content()
        );
    }

    public void testFallback() {
        assertEquals(
            "HelloWorld",
            new Fallback(
                () -> {
                    throw new InvariantViolation("err");
                },
                new Text.Of("HelloWorld")
            ).content()
        );
    }

    public void testFallbackError() {
        try {
            new Fallback(
                () -> {
                    throw new InvariantViolation("err 1");
                },
                () -> {
                    throw new InvariantViolation("err 2");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
