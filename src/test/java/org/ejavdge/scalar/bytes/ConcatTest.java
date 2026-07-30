package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;

public final class ConcatTest extends TestCase {
    public void testSingle() {
        assertEquals(
    "HelloWorld",
            new String(
                new Concat(
                    new Bytes.Of(
                        "HelloWorld".getBytes(StandardCharsets.US_ASCII)
                    )
                ).content(),
                StandardCharsets.US_ASCII
            )
        );
    }

    public void testSeveral() {
        assertEquals(
            "HelloWorld",
            new String(
                new Concat(
                    new Bytes.Of(
                        "Hello".getBytes(StandardCharsets.US_ASCII)
                    ),
                    new Bytes.Of(
                        "World".getBytes(StandardCharsets.US_ASCII)
                    )
                ).content(),
                StandardCharsets.US_ASCII
            )
        );
    }

    public void testEmpty() {
        assertEquals(
            0,
            new Concat().content().length
        );
    }
}
