package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Sid;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.FakeMedia;

import java.nio.charset.StandardCharsets;

public final class ContextOfSidTest extends TestCase {
    public void testSid() {
        assertEquals(
            "SID:1111:",
            new ContextOfSid(
                new Sid(
                    new Text.Of("1111")
                )
            ).imprint(new FakeMedia())
        );
    }

    public void testSession() {
        assertEquals(
            "SID:1111:",
            new ContextOfSid(
                new Session(
                    new Bytes.Of(
                        "localhost:90/?SID=1111"
                            .getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).imprint(new FakeMedia())
        );
    }

    public void testBytes() {
        assertEquals(
            "SID:1111:",
            new ContextOfSid(
                new Bytes.Of(
                    "localhost:90/?SID=1111"
                        .getBytes(StandardCharsets.UTF_8)
                )
            ).imprint(new FakeMedia())
        );
    }

    public void testByteArray() {
        assertEquals(
            "SID:1111:",
            new ContextOfSid(
                "localhost:90/?SID=1111"
                    .getBytes(StandardCharsets.UTF_8)
            ).imprint(new FakeMedia())
        );
    }
}
