package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;

import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public final class BytesAboutTest extends TestCase {
    public void testContent() {
        assertArrayEquals(
            "hello".getBytes(StandardCharsets.UTF_8),
            new BytesAbout(
                "greetings",
                new Bytes.Of(
                    "hello".getBytes(StandardCharsets.UTF_8)
                )
            ).content()
        );
    }

    public void testContextInViolation() {
        try {
            new BytesAbout(
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
            new BytesAbout(
                "unknown",
                new BytesAbout(
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
