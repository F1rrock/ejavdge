package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Ascii;

import java.nio.charset.StandardCharsets;

public final class AsciiTest extends TestCase {
    public void testAsciiText() {
        assertEquals(
            "HelloWorld",
            new String(
                new Ascii(
                    new Text.Of("HelloWorld")
                ).content(),
                StandardCharsets.US_ASCII
            )
        );
    }

    public void testNonAsciiText() {
        try {
            new Ascii(
                new Text.Of("ПриветМир")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
