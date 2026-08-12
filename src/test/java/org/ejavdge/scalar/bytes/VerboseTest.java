package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class VerboseTest extends TestCase {
    public void testMessageIsPrintedWhenCalled() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PrintStream original = System.out;
        System.setOut(
            new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
            )
        );

        try {
            final byte[] result = new Verbose(
                new Bytes.Of("Hello".getBytes(StandardCharsets.UTF_8)),
                "Fetching session..."
            ).content();
        } finally {
            System.setOut(original);
        }

        assertEquals(
            "Fetching session..." + System.lineSeparator(),
            output.toString(StandardCharsets.UTF_8)
        );
    }

    public void testWrappedResultIsPreserved() {
        final byte[] expected = "Hello".getBytes(StandardCharsets.UTF_8);

        assertEquals(
            new String(expected, StandardCharsets.UTF_8),
            new String(
                new Verbose(
                    new Bytes.Of(expected),
                    "message"
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testWrappedExceptionIsPreserved() {
        final InvariantViolation expected = new InvariantViolation("original");

        final Bytes failing = () -> {
            throw expected;
        };

        try {
            new Verbose(failing, "message").content();
        } catch (final InvariantViolation actual) {
            assertEquals(expected, actual);
            return;
        }

        fail("InvariantViolation");
    }
}