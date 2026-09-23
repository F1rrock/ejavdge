package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicInteger;

public final class NoticeTest extends TestCase {
    public void testReturnsOriginContent() {
        assertEquals(
            "Done!",
            new Notice(
                () -> {},
                new Text.Of("Done!")
            ).content()
        );
    }

    public void testPerformsEffect() {
        final var calls = new AtomicInteger(0);
        new Notice(
            calls::incrementAndGet,
            new Text.Of("Done!")
        ).content();
        assertEquals(1, calls.get());
    }

    public void testLazyUntilContent() {
        final var calls = new AtomicInteger(0);
        new Notice(
            calls::incrementAndGet,
            new Text.Of("Done!")
        );
        assertEquals(0, calls.get());
    }

    public void testEffectBeforeText() {
        final var order = new StringBuilder();
        new Notice(
            () -> order.append("effect,"),
            () -> {
                order.append("text");
                return "Done!";
            }
        ).content();
        assertEquals("effect,text", order.toString());
    }

    public void testTextNotReadOnBrokenEffect() {
        try {
            new Notice(
                () -> {
                    throw new InvariantViolation("There is no effect.");
                },
                () -> {
                    throw new AssertionError("Text must not be read");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEffect() {
        try {
            new Notice(
                () -> {
                    throw new InvariantViolation("There is no effect.");
                },
                new Text.Of("Done!")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenText() {
        try {
            new Notice(
                () -> {},
                () -> {
                    throw new InvariantViolation("There is no text.");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEffectCalledOnEveryContent() {
        final var calls = new AtomicInteger(0);
        final var notice = new Notice(
            calls::incrementAndGet,
            new Text.Of("Done!")
        );
        notice.content();
        notice.content();
        assertEquals(2, calls.get());
    }
}
