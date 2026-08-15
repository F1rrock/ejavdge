package org.ejavdge.web.media;

import junit.framework.TestCase;
import org.ejavdge.web.context.FakeContext;

public final class LiftTest extends TestCase {
    public void testMediaLifting() {
        assertEquals(
            "name 1:value 1:name 2:value 2:",
            new FakeContext().imprint(
                new Lift<>(
                    new FakeMedia()
                )
            ).content()
        );
    }

    public void testDoubleImprint() {
        assertEquals(
            "name 1:value 1:name 2:value 2:name 1:value 1:name 2:value 2:",
            new FakeContext().imprint(
                new FakeContext().imprint(
                    new Lift<>(
                        new FakeMedia()
                    )
                )
            )
        );
    }
}
