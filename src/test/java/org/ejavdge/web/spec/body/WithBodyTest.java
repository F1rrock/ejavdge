package org.ejavdge.web.spec.body;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.spec.HttpSpec;

import java.nio.charset.StandardCharsets;

public final class WithBodyTest extends TestCase {
    public void testContentLengthExistence() {
        assertTrue(
            new String(
                new WithBody(
                    new Bytes.Of(
                        "Body".getBytes(StandardCharsets.US_ASCII)
                    ),
                    new HttpSpec.Of(
                        "Spec".getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            ).contains("Content-Length: 4")
        );
    }

    public void testContentLengthCorrectness() {
        final var b = "Body".getBytes(StandardCharsets.US_ASCII);
        assertTrue(
            new String(
                new WithBody(
                    new Bytes.Of(b),
                    new HttpSpec.Of(
                        "Spec".getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            ).contains(String.format("Content-Length: %d", b.length))
        );
    }

    public void testTerminatorBeforeBody() {
        assertTrue(
            new String(
                new WithBody(
                    new Bytes.Of(
                        "Body".getBytes(StandardCharsets.US_ASCII)
                    ),
                    new HttpSpec.Of(
                        "Spec".getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            ).contains("\r\nBody")
        );
    }

    public void testStructure() {
        assertEquals(
            "Spec\r\nContent-Length: 4\r\n\r\nBody",
            new String(
                new WithBody(
                    new Bytes.Of(
                        "Body".getBytes(StandardCharsets.US_ASCII)
                    ),
                    new HttpSpec.Of(
                        "Spec\r\n".getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            )
        );
    }

    public void testEmptyOrigin() {
        try {
            new WithBody(
                new Bytes.Of(
                    "Body".getBytes(StandardCharsets.US_ASCII)
                ),
                new HttpSpec.Of(
                    new byte[0]
                )
            ).bytes();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyBody() {
        assertEquals(
            "Spec\r\nContent-Length: 0\r\n\r\n",
            new String(
                new WithBody(
                    new Bytes.Of(new byte[0]),
                    new HttpSpec.Of(
                        "Spec\r\n".getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            )
        );
    }
}
