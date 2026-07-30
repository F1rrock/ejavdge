package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.web.media.FakeMedia;

public final class LocationTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "url:/ejudge:host:0.0.0.0:port:90:",
            new Location(
                "/ejudge",
                "0.0.0.0",
                90
            ).imprint(new FakeMedia())
        );
    }
}
