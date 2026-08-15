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
                        "HelloWorld".getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testSeveral() {
        assertEquals(
            "HelloWorld",
            new String(
                new Concat(
                    new Bytes.Of(
                        "Hello".getBytes(StandardCharsets.UTF_8)
                    ),
                    new Bytes.Of(
                        "World".getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
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
