package org.ejavdge.web.spec.header;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.spec.HttpSpec;

import java.nio.charset.StandardCharsets;

public final class WithHeadersTest extends TestCase {
    public void testSeveralHeaders() {
        assertEquals(
            """
            beginning of the request...\r
            Content-Type: application/x-www-form-urlencoded\r
            Content-Length: 10\r
            """,
            new String(
                new WithHeaders(
                    new Items.Of<>(
                        new Header(
                            new Bytes.Of(
                                "Content-Type: application/x-www-form-urlencoded\r\n"
                                    .getBytes(StandardCharsets.US_ASCII)
                            )
                        ),
                        new Header(
                            new Bytes.Of(
                                "Content-Length: 10\r\n"
                                    .getBytes(StandardCharsets.US_ASCII)
                            )
                        )
                    ),
                    new HttpSpec.Of(
                        "beginning of the request...\r\n"
                            .getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            )
        );
    }

    public void testEmptyOrigin() {
        try {
            new WithHeaders(
                new Header(
                    new Bytes.Of(
                        "name: val\r\n".getBytes(StandardCharsets.US_ASCII)
                    )
                ),
                new HttpSpec.Of(
                    new Bytes.Of(new byte[0])
                )
            ).bytes();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyHeaders() {
        assertEquals(
            "origin",
            new String(
                new WithHeaders(
                    new Items.Of<>(),
                    new HttpSpec.Of(
                        new Bytes.Of(
                            "origin".getBytes(StandardCharsets.US_ASCII)
                        )
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            )
        );
    }
}
