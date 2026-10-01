package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class LocalProbeAppTest extends TestCase {
    public void testSuccessFeedbackIsGreen() {
        final var output = new StringBuilder();
        new LocalProbeApp(
            new VerdictOfProbe(() -> true),
            text -> output.append(text.content())
        ).run();
        assertEquals(
            "\u001B[32mSuccess: all local tests passed\u001B[0m",
            output.toString()
        );
    }

    public void testFailFeedbackIsRed() {
        final var output = new StringBuilder();
        new LocalProbeApp(
            new VerdictOfProbe(() -> false),
            text -> output.append(text.content())
        ).run();
        assertEquals(
            "\u001B[31mFail: some local tests failed\u001B[0m",
            output.toString()
        );
    }

    public void testVerdictCheckedOnce() {
        final var calls = new AtomicInteger(0);
        new LocalProbeApp(
            new VerdictOfProbe(() -> {
                calls.incrementAndGet();
                return true;
            }),
            Text::content
        ).run();
        assertEquals(1, calls.get());
    }

    public void testBrokenVerdictWritesError() {
        final var output = new StringBuilder();
        try {
            new LocalProbeApp(
                new VerdictOfProbe(() -> {
                    throw new InvariantViolation("There is no verdict.");
                }),
                text -> output.append(text.content())
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertTrue(output.toString().isEmpty());
        }
    }

    public void testOutputNotCalledBeforeRun() {
        final var calls = new AtomicInteger(0);
        new LocalProbeApp(
            new VerdictOfProbe(() -> true),
            text -> calls.incrementAndGet()
        );
        assertEquals(0, calls.get());
    }

    public void testFromEffect() {
        final var calls = new AtomicInteger(0);
        new LocalProbeApp(
            calls::incrementAndGet
        ).run();
        assertEquals(1, calls.get());
    }

    public void testFromBrokenEffect() {
        try {
            new LocalProbeApp(
                () -> {
                    throw new InvariantViolation("There is no effect.");
                }
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
