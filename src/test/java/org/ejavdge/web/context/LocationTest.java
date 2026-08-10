package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.FakeMedia;

public final class LocationTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "url:/ejudge:host:0.0.0.0:port:90:",
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("0.0.0.0"),
                new Num.Of(90)
            ).imprint(new FakeMedia())
        );
    }

    public void testNonPositivePort() {
        try {
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("0.0.0.0"),
                new Num.Of(-1)
            ).imprint(new FakeMedia());
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testQueryParams() {
        assertEquals(
            "url:/ejudge?name%201=value%201&name%202=value%202:host:0.0.0.0:port:90:",
            new Location(
                new WithEntry(
                    new Text.Of("name 1"),
                    new Text.Of("value 1"),
                    new WithEntry(
                        new Text.Of("name 2"),
                        new Text.Of("value 2")
                    )
                ),
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("0.0.0.0"),
                    new Num.Of(90)
                )
            ).imprint(new FakeMedia())
        );
    }
}
