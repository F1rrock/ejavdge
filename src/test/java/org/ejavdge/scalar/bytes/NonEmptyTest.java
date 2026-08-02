package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;

public final class NonEmptyTest extends TestCase {
    public void testHello() {
        assertEquals(
            "Hello",
            new String(
                new NonEmpty(
                    new Bytes.Of(
                        "Hello".getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testEmpty() {
        try {
            new NonEmpty(new Bytes.Of(new byte[0])).content();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
