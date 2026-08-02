package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.charset.StandardCharsets;

public final class Utf8TextTest extends TestCase {
    public void testAsciiBytes() {
        assertEquals(
            "HelloWorld",
            new Utf8Text(
                new Bytes.Of(
                    "HelloWorld".getBytes(StandardCharsets.UTF_8)
                )
            ).content()
        );
    }

    public void testCyrillicBytes() {
        assertEquals(
            "ПриветМир",
            new Utf8Text(
                new Bytes.Of(
                    "ПриветМир".getBytes(StandardCharsets.UTF_8)
                )
            ).content()
        );
    }

    public void testInvalidUtf8() {
        try {
            new Utf8Text(
                new Bytes.Of(
                    new byte[] {
                        (byte) 0xD0,
                        (byte) 0x28
                    }
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}