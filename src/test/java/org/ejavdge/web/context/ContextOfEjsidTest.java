package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Ejsid;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.FakeMedia;

import java.nio.charset.StandardCharsets;

public final class ContextOfEjsidTest extends TestCase {
    public void testEjsid() {
        assertEquals(
            "EJSID:1111:",
            new ContextOfEjsid(
                new Ejsid(
                    new Text.Of("1111")
                )
            ).imprint(new FakeMedia())
        );
    }

    public void testSession() {
        assertEquals(
            "EJSID:1111:",
            new ContextOfEjsid(
                new Session(
                    new Bytes.Of(
                        "EJSID=1111"
                            .getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).imprint(new FakeMedia())
        );
    }

    public void testBytes() {
        assertEquals(
            "EJSID:1111:",
            new ContextOfEjsid(
                new Bytes.Of(
                    "EJSID=1111"
                        .getBytes(StandardCharsets.UTF_8)
                )
            ).imprint(new FakeMedia())
        );
    }

    public void testByteArray() {
        assertEquals(
            "EJSID:1111:",
            new ContextOfEjsid(
                "EJSID=1111"
                    .getBytes(StandardCharsets.UTF_8)
            ).imprint(new FakeMedia())
        );
    }
}
