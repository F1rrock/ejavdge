package org.ejavdge.web.spec.body.multipart;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class BoundaryTest extends TestCase {
    public void testWithBoundaryId() {
        assertEquals(
            "----WebKitFormBoundary1111",
            new String(
                new Boundary(new Text.Of("1111")).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testBrokenId() {
        try {
            new Boundary((Text) () -> {
                throw new InvariantViolation("there is no id");
            }).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
