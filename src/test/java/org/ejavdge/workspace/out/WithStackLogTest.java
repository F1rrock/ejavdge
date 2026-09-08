package org.ejavdge.workspace.out;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithStackLogTest extends TestCase {
    public void testWithoutError() {
        final var log = new FakeLogger();
        log.setEnabled(true);
        new WithStackLog(
            t -> {},
            log
        ).write(new Text.Of("hello"));
        assertFalse(log.written());
    }

    public void testDisabledTrace() {
        final var log = new FakeLogger();
        log.setEnabled(false);
        new WithStackLog(
            t -> {},
            log
        ).write(new Text.Of("hello"));
        try {
            new WithStackLog(
                Text::content,
                log
            ).write(() -> {
                throw new InvariantViolation("there is no text");
            });
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertFalse(log.written());
        }
    }

    public void testFlatError() {
        final var log = new FakeLogger();
        log.setEnabled(true);
        try {
            new WithStackLog(
                Text::content,
                log
            ).write(() -> {
                throw new InvariantViolation("there is no text");
            });
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(
                """
                Stack trace:
                there is no text
                """,
                log.cache()
            );
        }
    }

    public void testDeepError() {
        final var log = new FakeLogger();
        log.setEnabled(true);
        try {
            new WithStackLog(
                Text::content,
                log
            ).write(() -> {
                throw new InvariantViolation(
                    "layer 1",
                    new InvariantViolation(
                        "layer 2",
                        new InvariantViolation("layer 3")
                    )
                );
            });
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(
                """
                Stack trace:
                layer 1
                layer 2
                layer 3
                """,
                log.cache()
            );
        }
    }
}
