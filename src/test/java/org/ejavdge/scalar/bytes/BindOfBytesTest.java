package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;

public final class BindOfBytesTest extends TestCase {
    public void testBindOfBytes() {
        assertEquals(
            "hello world",
            new String(
                new BindOfBytes(
                    new Bytes.Of(new byte[] {'h', 'e', 'l', 'l', 'o'}),
                    bs -> new Bytes.Of((
                            new String(bs, StandardCharsets.UTF_8) + " world"
                        ).getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testBindOfEmpty() {
        assertEquals(
            "fallback",
            new String(
                new BindOfBytes(
                    new Bytes.Of(new byte[0]),
                    s -> new Bytes.Of(new byte[] {
                        'f', 'a', 'l', 'l', 'b', 'a', 'c', 'k'
                    })
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testOriginThrows() {
        try {
            new BindOfBytes(
                () -> {
                    throw new InvariantViolation("origin error");
                },
                s -> {
                    throw new InvariantViolation("fallback error");
                }
            ).content();
            fail("InvariantViolation expected");
        } catch (final InvariantViolation e) {
            assertEquals("origin error", e.getMessage());
        }
    }

    public void testBindingThrows() {
        try {
            new BindOfBytes(
                new Bytes.Of(new byte[] {'h', 'e', 'l', 'l', 'o'}),
                s -> {
                    throw new InvariantViolation("binding error");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
