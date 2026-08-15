package org.ejavdge.web.media;

import junit.framework.TestCase;
import org.ejavdge.web.context.FakeContext;

import java.nio.charset.StandardCharsets;

public final class FormTest extends TestCase {
    public void testUrlForm() {
        assertEquals(
            "name%201=value%201&name%202=value%202",
            new String(
                new Form.ImprintOf(
                    new FakeContext()
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }
}
