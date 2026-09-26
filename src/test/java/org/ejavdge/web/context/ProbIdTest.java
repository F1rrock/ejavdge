package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.FakeMedia;

import java.util.concurrent.atomic.AtomicInteger;

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

    public void testOriginCalls() {
        final var calls = new AtomicInteger(0);
        new ProbId(() -> {
            calls.incrementAndGet();
            return 90;
        }).imprint(new FakeMedia());
        assertEquals(1, calls.get());
    }
}
