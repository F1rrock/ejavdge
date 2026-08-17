package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class VerboseTest extends TestCase {
    public void testWithLog() {
        final var log = new FakeLogger();
        log.setEnabled(true);
        new Verbose(
            new Bytes.Of(new byte[0]),
            "bla bla",
            log
        ).content();
        assertEquals("bla bla", log.cache());
    }

    public void testWithoutLog() {
        final var log = new FakeLogger();
        log.setEnabled(false);
        new Verbose(
            new Bytes.Of(new byte[0]),
            "bla bla",
            log
        ).content();
        assertFalse(log.written());
    }

    public void testErrorPropagation() {
        try {
            new Verbose(
                () -> {
                    throw new InvariantViolation("origin failed intentionally");
                },
                "what"
            ).content();
        } catch (final InvariantViolation e) {
            assertEquals("origin failed intentionally", e.getMessage());
            return;
        }
        fail("InvariantViolation");
    }
}
