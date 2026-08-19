package org.ejavdge.contest;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class PayloadTest extends TestCase {
    public void testWithHeaders() {
        assertEquals(
            "{ \"h\": 8, \"m\": 8, \"s\": 56, \"d\": 19, " +
            "\"o\": 8, \"y\": 2026, \"r\": 11667, \"z\": 1 }",
            new Payload(
                new Text.Of(
                    """
                        HTTP/1.1 200 OK\r
                        Transfer-Encoding: chunked\r
                        \r
                        { "h": 8, "m": 8, "s": 56, "d": 19, "o": 8, "y": 2026, "r": 11667, "z": 1 }"""
                )
            ).content()
        );
    }

    public void testWithoutTerminator() {
        assertEquals(
            "{ \"h\": 8, \"m\": 8, \"s\": 56, \"d\": 19, " +
                "\"o\": 8, \"y\": 2026, \"r\": 11667, \"z\": 1 }",
            new Payload(
                new Text.Of(
                    "{ \"h\": 8, \"m\": 8, \"s\": 56, \"d\": 19, " +
                        "\"o\": 8, \"y\": 2026, \"r\": 11667, \"z\": 1 }"
                )
            ).content()
        );
    }

    public void testBrokenOrigin() {
        try {
            new Payload(
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
