package org.ejavdge.web.spec.body.multipart;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class TextPartTest extends TestCase {
    public void testWithSid() {
        assertEquals(
            """
            Content-Disposition: form-data; name="SID"\r
            \r
            0df2b2e12d05ac44""",
            new String(
                new TextPart(
                    new Text.Of("SID"),
                    new Text.Of("0df2b2e12d05ac44")
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testWithoutName() {
        try {
            new TextPart(
                new Empty(),
                new Text.Of("bla bla")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutValue() {
        assertEquals(
            """
            Content-Disposition: form-data; name="no-val"\r
            \r
            """,
            new String(
                new TextPart(
                    new Text.Of("no-val"),
                    new Empty()
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }
}
