package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestForm;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.workspace.out.Out;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public final class SubmitWithNotificationIT extends TestCase {
    private static final byte[] SESSION = """
        HTTP/1.1 302 FOUND\r
        Set-Cookie: EJSID=756b423a0a6fe6a7;\r
        Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
        Content-Length: 2\r
        \r
        OK\r
        """.getBytes(StandardCharsets.UTF_8);

    private String main;
    private String problem;

    @Override
    protected void setUp() throws Exception {
        this.main = Files.readString(
            new File("src/test/resources/pages/main.html").toPath()
        );
        this.problem = Files.readString(
            new File("src/test/resources/pages/problem.html").toPath()
        );
    }

    public void testSubmissionAndNotifications() {
        final var output = new StringBuilder();
        final var driver = this.driver(new AtomicInteger(0));
        new SubmitWithNotification(
            new SilentSubmit(
                this.file(),
                this.form(driver),
                this.resource(driver)
            ),
            this.resource(driver),
            this.out(output)
        ).run();
        assertEquals(
            "Tested by Ejudge!Report is available!",
            output.toString()
        );
    }

    public void testPollingUntilReportIsReady() {
        final var statuses = new AtomicInteger();
        final var output = new StringBuilder();
        final var driver = this.pollingDriver(statuses);
        new SubmitWithNotification(
            new SilentSubmit(
                this.file(),
                this.form(driver),
                this.resource(driver)
            ),
            this.resource(driver),
            this.out(output)
        ).run();
        assertEquals(3, statuses.get());
        assertEquals(
            "Tested by Ejudge!Report is available!",
            output.toString()
        );
    }

    public void testBrokenSubmission() {
        try {
            new SubmitWithNotification(
                () -> {
                    throw new InvariantViolation(
                        "There is no submission."
                    );
                }
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenStatus() {
        final var output = new StringBuilder();
        final var posted = new AtomicInteger(0);
        try {
            final var driver = this.brokenStatusDriver(posted);
            new SubmitWithNotification(
                new SilentSubmit(
                    this.file(),
                    this.form(driver),
                    this.resource(driver)
                ),
                this.resource(driver),
                this.out(output)
            ).run();
            fail("InvariantViolation");
        } catch (final InvariantViolation e) {
            assertEquals(1, posted.get());
        }
    }

    private WebDriver driver(final AtomicInteger statuses) {
        return (loc, req) -> {
            final var request = new String(
                req.bytes(),
                StandardCharsets.UTF_8
            );
            if (request.startsWith("POST")) {
                return this.accepted();
            }
            if (request.contains("action=175")) {
                statuses.incrementAndGet();
                return this.readyStatus();
            }
            if (request.contains("action=139")) {
                return this.problem.getBytes(StandardCharsets.UTF_8);
            }
            return this.main.getBytes(StandardCharsets.UTF_8);
        };
    }

    private WebDriver pollingDriver(final AtomicInteger statuses) {
        return (loc, req) -> {
            final var request = new String(
                req.bytes(),
                StandardCharsets.UTF_8
            );
            if (request.startsWith("POST")) {
                return this.accepted();
            }
            if (request.contains("action=175")) {
                if (statuses.incrementAndGet() < 3) {
                    return this.pendingStatus();
                }
                return this.readyStatus();
            }
            if (request.contains("action=139")) {
                return this.problem.getBytes(StandardCharsets.UTF_8);
            }
            return this.main.getBytes(StandardCharsets.UTF_8);
        };
    }

    private WebDriver brokenStatusDriver(final AtomicInteger posted) {
        return (loc, req) -> {
            final var request = new String(
                req.bytes(),
                StandardCharsets.UTF_8
            );
            if (request.startsWith("POST")) {
                posted.incrementAndGet();
                return this.accepted();
            }
            if (request.contains("action=175")) {
                throw new InvariantViolation("There is no status.");
            }
            if (request.contains("action=139")) {
                return this.problem.getBytes(StandardCharsets.UTF_8);
            }
            return this.main.getBytes(StandardCharsets.UTF_8);
        };
    }

    private byte[] accepted() {
        return """
            HTTP/1.1 302 FOUND\r
            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2\r
            Content-Length: 0\r
            \r
            """.getBytes(StandardCharsets.UTF_8);
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

    private byte[] readyStatus() {
        return """
            HTTP/1.1 200 OK\r
            Content-Length: 65\r
            Content-Type: text/plain; charset=utf-8\r
            \r
            { "h": 15, "m": 26, "s": 9, "d": 29, "o": 9, "y": 2026, "z": 1 }"""
            .getBytes(StandardCharsets.UTF_8);
    }

    private Out out(final StringBuilder output) {
        return text -> output.append(text.content());
    }

    private ByteFile file() {
        return new ByteFile.Of(
            new Text.Of("problem"),
            new Bytes.Of(
                """
                // problem: WithLinks
                // language: 2
                int main() {}
                """.getBytes(StandardCharsets.UTF_8)
            )
        );
    }

    private ContestForm form(final WebDriver driver) {
        return new ContestForm(
            driver,
            this.location(),
            this.session()
        );
    }

    private ContestResource resource(final WebDriver driver) {
        return new ContestResource(
            driver,
            this.location(),
            this.session()
        );
    }

    private Location location() {
        return new Location(
            new Text.Of("/ejudge"),
            new Text.Of("localhost"),
            new Num.Of(90)
        );
    }

    private Session session() {
        return new Session(new Bytes.Of(SESSION));
    }
}
