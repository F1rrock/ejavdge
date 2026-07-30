package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.FakeMedia;

public final class HostTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "host:0.0.0.0:",
            new Location.Host(
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("0.0.0.0"),
                    new Num.Of(90)
                )
            ).imprint(new FakeMedia())
        );
    }
}
