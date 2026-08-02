package org.ejavdge.web.driver;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.media.FakeMedia;
import org.ejavdge.web.spec.Request;

import java.nio.charset.StandardCharsets;

public final class FakeDriver implements WebDriver {
    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        return (
            loc.imprint(new FakeMedia()) + " "
            + new String(req.bytes(), StandardCharsets.UTF_8)
        ).getBytes(StandardCharsets.UTF_8);
    }
}
