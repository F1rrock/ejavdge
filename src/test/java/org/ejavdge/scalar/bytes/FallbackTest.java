package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.Arrays;

public final class FallbackTest extends TestCase {
    public void testEmptyPrimaryContent() {
        assertEquals(
            0,
            new Fallback(
                new Bytes.Of(new byte[0]),
                () -> {
                    throw new AssertionError("Fallback evaluated after primary success");
                }
            ).content().length
        );
    }

    public void testFallbackContent() {
        final byte[] expected = {0, (byte) 0xff};
        assertTrue(
            Arrays.equals(
                expected,
                new Fallback(
                    () -> {
                        throw new InvariantViolation("There are no primary bytes.");
                    },
                    new Bytes.Of(expected)
                ).content()
            )
        );
    }

    public void testFallbackError() {
        final var error = new InvariantViolation("There are no fallback bytes.");
        try {
            new Fallback(
                () -> {
                    throw new InvariantViolation("There are no primary bytes.");
                },
                () -> {
                    throw error;
                }
            ).content();
        } catch (final InvariantViolation e) {
            assertSame(error, e);
            return;
        }
        fail("InvariantViolation");
    }
}
