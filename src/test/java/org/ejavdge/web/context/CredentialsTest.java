package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.FakeMedia;

public final class CredentialsTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "login:vader:password:ejudge:contest_id:1:",
            new Credentials(
                "vader",
                "ejudge",
                1
            ).imprint(new FakeMedia())
        );
    }

    public void testNonPositiveContestId() {
        try {
            new Credentials(
                "vader",
                "ejudge",
                -1
            ).imprint(new FakeMedia());
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
