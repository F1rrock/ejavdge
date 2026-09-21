package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class FeedbackTest extends TestCase {
    public void testSuccess() {
        assertEquals(
            "pass",
            new Feedback(
                new Text.Of("pass"),
                new Text.Of("fail"),
                () -> true
            ).content()
        );
    }

    public void testFail() {
        assertEquals(
            "fail",
            new Feedback(
                new Text.Of("pass"),
                new Text.Of("fail"),
                () -> false
            ).content()
        );
    }

    public void testOnlySuccessEvaluated() {
        try {
            assertEquals(
                "pass",
                new Feedback(
                    new Text.Of("pass"),
                    () -> {
                        throw new InvariantViolation("fail must not be read");
                    },
                    () -> true
                ).content()
            );
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testOnlyFailEvaluated() {
        try {
            assertEquals(
                "fail",
                new Feedback(
                    () -> {
                        throw new InvariantViolation("success must not be read");
                    },
                    new Text.Of("fail"),
                    () -> false
                ).content()
            );
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testLazyVerdict() {
        final var calls = new AtomicInteger(0);
        new Feedback(
            new Text.Of("pass"),
            new Text.Of("fail"),
            () -> {
                calls.incrementAndGet();
                return true;
            }
        );
        assertEquals(0, calls.get());
    }

    public void testVerdictEvaluatedOnContent() {
        final var calls = new java.util.concurrent.atomic.AtomicInteger(0);
        new Feedback(
            new Text.Of("pass"),
            new Text.Of("fail"),
            () -> {
                calls.incrementAndGet();
                return true;
            }
        ).content();
        assertEquals(1, calls.get());
    }

    public void testBrokenVerdict() {
        try {
            new Feedback(
                new Text.Of("pass"),
                new Text.Of("fail"),
                () -> {
                    throw new InvariantViolation("There is no verdict.");
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenSuccess() {
        try {
            new Feedback(
                () -> {
                    throw new InvariantViolation("There is no success.");
                },
                new Text.Of("fail"),
                () -> true
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenFail() {
        try {
            new Feedback(
                new Text.Of("pass"),
                () -> {
                    throw new InvariantViolation("There is no fail.");
                },
                () -> false
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
