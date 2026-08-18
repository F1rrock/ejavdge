package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.FakeMedia;

public final class ProbIdTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "prob_id:90:",
            new ProbId(90).imprint(new FakeMedia())
        );
    }

    public void testNonPositiveId() {
        try {
            new ProbId(0).imprint(new FakeMedia());
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
