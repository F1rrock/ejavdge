package org.ejavdge.web.spec.body.multipart;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.FakeContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

public final class TextPartsTest extends TestCase {
    public void testWithoutParts() {
        assertEquals(List.of(), new TextParts().content());
    }

    public void testSinglePart() {
        assertEquals(
            List.of(
                """
                Content-Disposition: form-data; name="SID"\r
                \r
                0df2b2e12d05ac44"""
            ),
            new TextParts()
                .with(
                    new Text.Of("SID"),
                    new Text.Of("0df2b2e12d05ac44")
                ).content()
                    .stream()
                    .map(Part::content)
                    .map(bs -> new String(bs, StandardCharsets.UTF_8))
                    .toList()
        );
    }

    public void testMultipleParts() {
        assertEquals(
            List.of(
                """
                Content-Disposition: form-data; name="SID"\r
                \r
                0df2b2e12d05ac44""",
                """
                Content-Disposition: form-data; name="prob_id"\r
                \r
                1"""
            ),
            new TextParts()
                .with(
                    new Text.Of("SID"),
                    new Text.Of("0df2b2e12d05ac44")
                )
                .with(
                    new Text.Of("prob_id"),
                    new Text.Of("1")
                ).content()
                .stream()
                .map(Part::content)
                .map(bs -> new String(bs, StandardCharsets.UTF_8))
                .toList()
        );
    }

    public void testImprint() {
        assertEquals(
            List.of(
                """
                Content-Disposition: form-data; name="name 1"\r
                \r
                value 1""",
                """
                Content-Disposition: form-data; name="name 2"\r
                \r
                value 2"""
            ),
            new TextParts.ImprintOf(
                new FakeContext()
            ).contents()
                .stream()
                .map(Part::content)
                .map(bs -> new String(bs, StandardCharsets.UTF_8))
                .toList()
        );
    }
}
