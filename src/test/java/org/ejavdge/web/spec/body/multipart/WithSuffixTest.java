package org.ejavdge.web.spec.body.multipart;

import junit.framework.TestCase;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.charset.StandardCharsets;

public final class WithSuffixTest extends TestCase {
    public void testStandardSuffix() {
        assertEquals(
            "------WebKitFormBoundary7MA4YWxkTrZu0gW--",
            new String(
                new WithSuffix(
                    new Bytes.Of(
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW"
                            .getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testCustomPrefix() {
        assertEquals(
            "======WebKitFormBoundary7MA4YWxkTrZu0gW==",
            new String(
                new WithSuffix(
                    new Bytes.Of(
                        "==".getBytes(StandardCharsets.UTF_8)
                    ),
                    new Bytes.Of(
                        "======WebKitFormBoundary7MA4YWxkTrZu0gW"
                            .getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }
}
