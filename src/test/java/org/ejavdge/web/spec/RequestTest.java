package org.ejavdge.web.spec;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;

public final class RequestTest extends TestCase {
    public void testEnding() {
        assertTrue(
            new String(
                new Request(
                    new HttpSpec.Of(
                        "Hello, World!".getBytes(StandardCharsets.UTF_8)
                    )
                ).bytes(),
                StandardCharsets.UTF_8
            ).endsWith("\r\n")
        );
    }
}
