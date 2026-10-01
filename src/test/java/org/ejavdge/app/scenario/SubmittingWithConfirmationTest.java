package org.ejavdge.app.scenario;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class SubmittingWithConfirmationTest extends TestCase {
    public void testSubmitCalls() {
        final var calls = new AtomicInteger(0);
        new SubmittingWithConfirmation(
            new SubmittingOfSolution(calls::incrementAndGet),
            this.resource(
                (loc, req) -> this.readyStatus()
            ),
            Duration.ZERO
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testPolling() {
        final var calls = new AtomicInteger(0);
        new SubmittingWithConfirmation(
            new SubmittingOfSolution(calls::incrementAndGet),
            this.resource(
                (loc, req) -> {
                    if (calls.incrementAndGet() < 5) {
                        return this.pendingStatus();
                    } else {
                        return this.readyStatus();
                    }
                }
            ),
            Duration.ZERO
        ).perform();
        assertEquals(5, calls.get());
    }

    public void testBrokenSubmission() {
        try {
            new SubmittingWithConfirmation(
                new SubmittingOfSolution(() -> {
                    throw new InvariantViolation("There is incorrect submission");
                }),
                this.resource(
                    (loc, req) -> this.readyStatus()
                ),
                Duration.ZERO
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            new SubmittingWithConfirmation(
                new SubmittingOfSolution(() -> {}),
                this.resource((loc, req) -> {
                    throw new InvariantViolation("There is incorrect driver");
                }),
                Duration.ZERO
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private ContestResource resource(final WebDriver d) {
        return new ContestResource(
            d,
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("localhost"),
                new Num.Of(90)
            ),
            new Session(
                new Bytes.Of(
                    """
                    HTTP/1.1 302 FOUND\r
                    Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                    Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                    Content-Length: 2\r
                    \r
                    OK\r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            )
        );
    }

    private byte[] readyStatus() {
        return """
            HTTP/1.1 200 OK\r
            Content-Length: 65\r
            Content-Type: text/plain; charset=utf-8\r
            \r
            { "h": 15, "m": 26, "s": 9, "d": 29, "o": 9, "y": 2026, "z": 1 }"""
            .getBytes(StandardCharsets.UTF_8);
    }

    private byte[] pendingStatus() {
        return """
            HTTP/1.1 200 OK\r
            Content-Length: 56\r
            Content-Type: text/plain; charset=utf-8\r
            \r
            { "h": 15, "m": 26, "s": 9, "d": 29, "o": 9, "y": 2026 }"""
            .getBytes(StandardCharsets.UTF_8);
    }
}
