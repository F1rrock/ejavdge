package org.ejavdge.domain.run;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public final class VerdictOfProbeIT extends TestCase {
    private static final String CORRECT = """
        // problem: WithLinks
        import java.util.Scanner;
        public class Main {
            public static void main(String[] args) {
                Scanner s = new Scanner(System.in);
                System.out.println(s.nextInt() + s.nextInt());
            }
        }
        """;

    private static final String WRONG = """
        // problem: WithLinks
        public class Main {
            public static void main(String[] args) {
                System.out.println(0);
            }
        }
        """;

    public void testPassesOnCorrectSolution() {
        try {
            final var main = this.mainPage();
            final var problem = this.problemPage();
            assertTrue(
                new VerdictOfProbe(
                    new JavaProgram(
                        this.file(CORRECT),
                        new Text.Of(".")
                    ),
                    this.resource(
                        (loc, req) -> new String(req.bytes(), StandardCharsets.UTF_8)
                            .contains("prob_id=3")
                            ? problem
                            : main
                    )
                ).ok()
            );
        } catch (final IOException | InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testFailsOnWrongSolution() {
        try {
            final var main = this.mainPage();
            final var problem = this.problemPage();
            assertFalse(
                new VerdictOfProbe(
                    new JavaProgram(
                        this.file(WRONG),
                        new Text.Of(".")
                    ),
                    this.resource(
                        (loc, req) -> new String(req.bytes(), StandardCharsets.UTF_8)
                            .contains("prob_id=3")
                            ? problem
                            : main
                    )
                ).ok()
            );
        } catch (final IOException | InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testProblemPageCalls() {
        try {
            final var main = this.mainPage();
            final var problem = this.problemPage();
            final var calls = new AtomicInteger(0);
            new VerdictOfProbe(
                new JavaProgram(
                    this.file(CORRECT),
                    new Text.Of(".")
                ),
                this.resource(
                    (loc, req) -> {
                        if (new String(req.bytes(), StandardCharsets.UTF_8)
                            .contains("prob_id=3")) {
                            calls.incrementAndGet();
                            return problem;
                        }
                        return main;
                    }
                )
            ).ok();
            assertEquals(1, calls.get());
        } catch (final IOException | InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testMainPageCalls() {
        try {
            final var main = this.mainPage();
            final var problem = this.problemPage();
            final var calls = new AtomicInteger(0);
            new VerdictOfProbe(
                new JavaProgram(
                    this.file(CORRECT),
                    new Text.Of(".")
                ),
                this.resource(
                    (loc, req) -> {
                        if (new String(req.bytes(), StandardCharsets.UTF_8)
                            .contains("prob_id=3")) {
                            return problem;
                        }
                        calls.incrementAndGet();
                        return main;
                    }
                )
            ).ok();
            assertEquals(1, calls.get());
        } catch (final IOException | InvariantViolation e) {
            throw new AssertionError(e);
        }
    }

    public void testUnknownProblem() {
        try {
            final var main = this.mainPage();
            final var problem = this.problemPage();
            new VerdictOfProbe(
                new JavaProgram(
                    this.file("// problem: NonexistentProblem\n"),
                    new Text.Of(".")
                ),
                this.resource(
                    (loc, req) -> new String(req.bytes(), StandardCharsets.UTF_8)
                        .contains("prob_id=3")
                        ? problem
                        : main
                )
            ).ok();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        fail("InvariantViolation");
    }

    public void testInvalidSession() {
        try {
            final var main = this.mainPage();
            final var problem = this.problemPage();
            new VerdictOfProbe(
                new JavaProgram(
                    this.file(CORRECT),
                    new Text.Of(".")
                ),
                new ContestResource(
                    (loc, req) -> new String(req.bytes(), StandardCharsets.UTF_8)
                        .contains("prob_id=3")
                        ? problem
                        : main,
                    this.location(),
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
                )
            ).ok();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            new VerdictOfProbe(
                new JavaProgram(
                    this.file(CORRECT),
                    new Text.Of(".")
                ),
                this.resource(
                    (loc, req) -> {
                        throw new InvariantViolation("there is no resources.");
                    }
                )
            ).ok();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testFromVerdict() {
        assertTrue(
            new VerdictOfProbe(() -> true).ok()
        );
    }

    public void testFromBrokenVerdict() {
        try {
            new VerdictOfProbe(
                () -> {
                    throw new InvariantViolation("There is no verdict.");
                }
            ).ok();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private byte[] mainPage() throws IOException {
        return Files.readString(
            new File(
                "src/test/resources/pages/main.html"
            ).toPath()
        ).getBytes(StandardCharsets.UTF_8);
    }

    private byte[] problemPage() throws IOException {
        return Files.readString(
            new File(
                "src/test/resources/pages/problem.html"
            ).toPath()
        ).getBytes(StandardCharsets.UTF_8);
    }

    private ByteFile file(final String source) {
        return new ByteFile.Of(
            new Text.Of("Main.java"),
            new Bytes.Of(source.getBytes(StandardCharsets.UTF_8))
        );
    }

    private ContestResource resource(final WebDriver driver) {
        return new ContestResource(
            driver,
            this.location(),
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

    private Location location() {
        return new Location(
            new Text.Of("/ejudge"),
            new Text.Of("localhost"),
            new Num.Of(90)
        );
    }
}
