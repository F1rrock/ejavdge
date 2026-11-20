package org.ejavdge.domain.run;

import junit.framework.TestCase;
import org.ejavdge.app.setup.PresetEngine;
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
import org.ejavdge.web.spec.Request;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VerdictOfProbeIT extends TestCase {
    private static final String MAIN_PAGE = "amp%3Baction=2";
    private static final String PROBLEM_PAGE = "action=139";
    private static final String HEADER = "HeaderOfWithLinks.java";
    private static final String FOOTER = "FooterOfWithLinks.java";

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

    public void testCorrectSolutionWithoutTemplate() throws IOException {
        assertTrue(this.probe(CORRECT, this.resource(this.driver())).ok());
    }

    public void testWrongSolution() throws IOException {
        assertFalse(this.probe(WRONG, this.resource(this.driver())).ok());
    }

    public void testSingleRequestPerPageAndTemplateFile() throws IOException {
        final var driver = this.driver(
            this.attachment(
                HEADER,
                """
                import java.util.Scanner;
                public class Main {
                    public static void main(String[] args) {
                        Scanner input = new Scanner(System.in);
                """
            ),
            this.attachment(
                FOOTER,
                """
                        System.out.println(result);
                    }
                }
                """
            )
        );
        assertTrue(
            this.probe(
                """
                // problem: WithLinks
                int result = input.nextInt() + input.nextInt();
                """,
                this.resource(driver)
            ).ok()
        );
        assertEquals("main page", 1, driver.calls(MAIN_PAGE));
        assertEquals("problem page", 1, driver.calls(PROBLEM_PAGE));
        assertEquals(HEADER, 1, driver.calls(HEADER));
        assertEquals(FOOTER, 1, driver.calls(FOOTER));
    }

    public void testUnknownProblem() throws IOException {
        final var driver = this.driver();
        try {
            this.probe("// problem: NonexistentProblem\n", this.resource(driver)).ok();
        } catch (final InvariantViolation e) {
            assertEquals("main page", 1, driver.calls(MAIN_PAGE));
            assertEquals("problem page", 0, driver.calls(PROBLEM_PAGE));
            assertEquals(HEADER, 0, driver.calls(HEADER));
            assertEquals(FOOTER, 0, driver.calls(FOOTER));
            return;
        }
        fail("InvariantViolation");
    }

    public void testInvalidSession() {
        try {
            this.probe(
                CORRECT,
                new ContestResource(
                    (loc, req) -> {
                        req.bytes();
                        throw new AssertionError("A request was assembled with an invalid session");
                    },
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
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            this.probe(
                CORRECT,
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

    private VerdictOfProbe probe(final String source, final ContestResource resource) {
        return new VerdictOfProbe(
            new JavaProgram(this.file(source), new Text.Of(".")),
            new PresetEngine(),
            resource
        );
    }

    private ProbeDriver driver() throws IOException {
        final byte[] missing = """
            HTTP/1.1 200 OK\r
            Content-Type: text/html; charset=UTF-8\r
            \r
            <html><body>Attachment not found</body></html>
            """.getBytes(StandardCharsets.UTF_8);
        return this.driver(missing, missing);
    }

    private ProbeDriver driver(final byte[] header, final byte[] footer) throws IOException {
        return new ProbeDriver(
            Map.of(
                MAIN_PAGE, this.page("main"),
                PROBLEM_PAGE, this.page("problem"),
                HEADER, header,
                FOOTER, footer
            )
        );
    }

    private byte[] page(final String name) throws IOException {
        return Files.readAllBytes(Path.of("src", "test", "resources", "pages", name + ".html"));
    }

    private byte[] attachment(final String name, final String source) {
        return """
            HTTP/1.1 200 OK\r
            Content-Disposition: attachment; filename="%s"\r
            \r
            %s""".formatted(name, source).getBytes(StandardCharsets.UTF_8);
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

    private static final class ProbeDriver implements WebDriver {
        private final Map<String, byte[]> replies;
        private final Map<String, Integer> counts = new HashMap<>();

        ProbeDriver(final Map<String, byte[]> responses) {
            this.replies = responses;
        }

        @Override
        public byte[] resourceOf(final Location loc, final Request req) {
            final String uri = new String(req.bytes(), StandardCharsets.UTF_8).split(" ", 3)[1];
            final var query = List.of(URI.create(uri).getRawQuery().split("&"));
            final String route = this.route(query);
            this.counts.merge(route, 1, Integer::sum);
            return this.replies.get(route);
        }

        private String route(final List<String> query) {
            if (query.contains(MAIN_PAGE)) {
                return MAIN_PAGE;
            }
            if (query.contains("prob_id=3")) {
                if (query.contains(PROBLEM_PAGE)) {
                    return PROBLEM_PAGE;
                }
                if (query.contains("action=194")) {
                    if (query.contains("file=" + HEADER)) {
                        return HEADER;
                    }
                    if (query.contains("file=" + FOOTER)) {
                        return FOOTER;
                    }
                }
            }
            throw new AssertionError("Unexpected probe request: " + query);
        }

        int calls(final String route) {
            return this.counts.getOrDefault(route, 0);
        }
    }
}
