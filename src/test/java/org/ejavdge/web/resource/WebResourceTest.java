package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.FakeDriver;
import org.ejavdge.web.spec.Request;

import java.nio.charset.StandardCharsets;

public final class WebResourceTest extends TestCase {
    public void testUrlForm() {
        assertEquals(
            "url:u:host:h:port:80: request",
            new String(
                new WebResource(
                    new FakeDriver(),
                    new Location(
                        new Text.Of("u"),
                        new Text.Of("h"),
                        new Num.Of(80)
                    ),
                    new Request(
                        new Bytes.Of(
                            "request".getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }
}
