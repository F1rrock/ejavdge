package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.FakeMedia;

public final class LangIdTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "lang_id:1:",
            new LangId(1).imprint(new FakeMedia())
        );
    }

    public void testNonPositiveId() {
        try {
            new LangId(0).imprint(new FakeMedia());
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
