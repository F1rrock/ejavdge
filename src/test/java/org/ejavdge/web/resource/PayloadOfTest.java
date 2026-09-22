package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class PayloadOfTest extends TestCase {
    public void testWithHeaders() {
        assertEquals(
            "{ \"h\": 8, \"m\": 8, \"s\": 56, \"d\": 19, " +
            "\"o\": 8, \"y\": 2026, \"r\": 11667, \"z\": 1 }",
            new String(
                new PayloadOf(
                    new Bytes.Of(
                        """
                            HTTP/1.1 200 OK\r
                            Transfer-Encoding: chunked\r
                            \r
                            { "h": 8, "m": 8, "s": 56, "d": 19, "o": 8, "y": 2026, "r": 11667, "z": 1 }"""
                                .getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testWithoutTerminator() {
        assertEquals(
            "{ \"h\": 8, \"m\": 8, \"s\": 56, \"d\": 19, " +
                "\"o\": 8, \"y\": 2026, \"r\": 11667, \"z\": 1 }",
            new String(
                new PayloadOf(
                    new Bytes.Of(
                        ("{ \"h\": 8, \"m\": 8, \"s\": 56, \"d\": 19, " +
                            "\"o\": 8, \"y\": 2026, \"r\": 11667, \"z\": 1 }")
                                .getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testBrokenOrigin() {
        try {
            new PayloadOf(
                () -> {
                    throw new InvariantViolation("there is no text");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
