package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.workspace.out.Out;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public final class AlreadySolvedIT extends TestCase {
    private String main;
    private String problem;
    private Location location;

    @Override
    protected void setUp() throws IOException {
        this.main = Files.readString(
            new File("src/test/resources/pages/main.html").toPath()
        );
        this.problem = Files.readString(
            new File("src/test/resources/pages/problem.html").toPath()
        );
        this.location = new Location(
            new Text.Of("/ejudge"),
            new Text.Of("localhost"),
            new Num.Of(90)
        );
    }

    private ContestResource validResource(final WebDriver d) {
        return new ContestResource(
            d,
            this.location,
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

    private boolean containsProblem(final String n) {
        try {
            final WebDriver driver = (loc, req) -> {
                final var request = new String(
                    req.bytes(),
                    StandardCharsets.UTF_8
                );
                if (request.contains("prob_id=")) {
                    return this.problem.getBytes(StandardCharsets.UTF_8);
                }
                return this.main.getBytes(StandardCharsets.UTF_8);
            };
            final var out = new CapturingOut();
            new AlreadySolved(
                this.validResource(driver),
                out
            ).run();
            return out.text().contains(n);
        } catch (final InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testProblemA() {
        assertTrue(
            containsProblem("A")
        );
    }

    public void testProblemB() {
        assertFalse(
            containsProblem("B")
        );
    }

    public void testProblemWithLinks() {
        assertFalse(
            containsProblem("WithLinks")
        );
    }

    public void testMainPageCalls() {
        try {
            final var calls = new AtomicInteger(0);
            new AlreadySolved(
                this.validResource(
                    (loc, req) -> {
                        if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=")) {
                            return this.problem.getBytes(StandardCharsets.UTF_8);
                        }
                        calls.incrementAndGet();
                        return this.main.getBytes(StandardCharsets.UTF_8);
                    }
                ),
                Text::content
            ).run();
            assertEquals(1, calls.get());
        } catch (final InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testAnyPageCalls() {
        try {
            final var calls = new AtomicInteger(0);
            new AlreadySolved(
                this.validResource(
                    (loc, req) -> {
                        calls.incrementAndGet();
                        if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=")) {
                            return this.problem.getBytes(StandardCharsets.UTF_8);
                        }
                        return this.main.getBytes(StandardCharsets.UTF_8);
                    }
                ),
                Text::content
            ).run();
            assertEquals(1, calls.get());
        } catch (final InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testInvalidSession() {
        try {
            new AlreadySolved(
                new ContestResource(
                    (loc, req) -> {
                        if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=")) {
                            return this.problem.getBytes(StandardCharsets.UTF_8);
                        }
                        return this.main.getBytes(StandardCharsets.UTF_8);
                    },
                    this.location,
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 200 OK\r
                            Location: http://localhost:90/ejudge&action=2&lt=1\r
                            Content-Length: 15\r
                            \r
                            Invalid session\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            new AlreadySolved(
                this.validResource(
                    (loc, req) -> {
                        throw new InvariantViolation("there is no resources.");
                    }
                ),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWrongPage() {
        final var out = new CapturingOut();
        new AlreadySolved(
            this.validResource(
                (loc, req) -> {
                    if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=")) {
                        return this.problem.getBytes(StandardCharsets.UTF_8);
                    }
                    return """
                    <html>
                        <body>
                            <p>404 Not found</p>
                        </body>
                    </html>
                    """.getBytes(StandardCharsets.UTF_8);
                }
            ),
            out
        ).run();
        assertEquals("You have not solved anything yet :(", out.text());
    }

    private static final class CapturingOut implements Out {
        private final StringBuilder buffer = new StringBuilder();

        @Override
        public void write(final Text t) {
            this.buffer.append(t.content());
        }

        String text() {
            return this.buffer.toString();
        }
    }
}
