package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.FakeMedia;

public final class RunIdTest extends TestCase {
    public void testPositiveId() {
        assertEquals(
            "run_id:90:",
            new RunId(90).imprint(new FakeMedia())
        );
    }

    public void testZeroId() {
        assertEquals(
            "run_id:0:",
            new RunId(0).imprint(new FakeMedia())
        );
    }

    public void testNegativeId() {
        try {
            new RunId(-5).imprint(new FakeMedia());
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
