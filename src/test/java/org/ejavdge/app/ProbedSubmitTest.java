package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.domain.run.VerdictOfProbe;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.workspace.out.Out;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProbedSubmitTest extends TestCase {
    public void testOrderOfEffects() {
        final var calls = new StringBuilder();
        new ProbedSubmit(
            new VerdictOfProbe(() -> {
                calls.append("Probe,");
                return true;
            }),
            new ReportedSubmit(() -> calls.append("Submit,")),
            this.out(calls)
        ).run();
        assertEquals(
            "Probe,All local tests passed.Submit,",
            calls.toString()
        );
    }

    public void testPassedProbeRunsSubmit() {
        final var submitted = new AtomicBoolean(false);
        new ProbedSubmit(
            new VerdictOfProbe(() -> true),
            new ReportedSubmit(() -> submitted.set(true)),
            this.out(new StringBuilder())
        ).run();
        assertTrue(submitted.get());
    }

    public void testFailedProbeSkipsSubmit() {
        final var submitted = new AtomicBoolean(false);
        new ProbedSubmit(
            new VerdictOfProbe(() -> false),
            new ReportedSubmit(() -> submitted.set(true)),
            this.out(new StringBuilder())
        ).run();
        assertFalse(submitted.get());
    }

    public void testMessageOfFailedProbe() {
        final var output = new StringBuilder();
        new ProbedSubmit(
            new VerdictOfProbe(() -> false),
            new ReportedSubmit(() -> {}),
            this.out(output)
        ).run();
        assertTrue(output.toString().contains("Some local tests failed."));
    }

    public void testBrokenVerdictSkipsSubmit() {
        final var submitted = new AtomicBoolean(false);
        new ProbedSubmit(
            new VerdictOfProbe(() -> {
                throw new InvariantViolation("Can not probe.");
            }),
            new ReportedSubmit(() -> submitted.set(true)),
            this.out(new StringBuilder())
        ).run();
        assertFalse(submitted.get());
    }

    public void testBrokenVerdictWritesError() {
        final var output = new StringBuilder();
        new ProbedSubmit(
            new VerdictOfProbe(() -> {
                throw new InvariantViolation("Can not probe.");
            }),
            new ReportedSubmit(() -> {}),
            this.out(output)
        ).run();
        assertTrue(output.toString().contains("Can not probe."));
    }

    public void testBrokenSubmitWritesSuccessThenError() {
        final var output = new StringBuilder();
        new ProbedSubmit(
            new VerdictOfProbe(() -> true),
            new ReportedSubmit(() -> {
                throw new InvariantViolation("Report failed");
            }),
            this.out(output)
        ).run();
        assertTrue(output.toString().contains("All local tests passed."));
        assertTrue(output.toString().contains("Report failed"));
    }

    public void testFromEffect() {
        final var calls = new AtomicInteger(0);
        new ProbedSubmit(
            calls::incrementAndGet
        ).run();
        assertEquals(1, calls.get());
    }

    private Out out(final StringBuilder sb) {
        return text -> {
            try {
                sb.append(text.content());
            } catch (final InvariantViolation e) {
                sb.append(e.getMessage());
            }
        };
    }
}
