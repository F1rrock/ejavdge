package org.ejavdge.web.spec;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;

public final class RequestTest extends TestCase {
    public void testEnding() {
        assertTrue(
            new String(
                new Request(
                    new HttpSpec.Of(
                        "Hello, World!".getBytes(StandardCharsets.US_ASCII)
                    )
                ).bytes(),
                StandardCharsets.US_ASCII
            ).endsWith("\r\n")
        );
    }
}
