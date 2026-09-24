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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;

public final class SilentSubmitIT extends TestCase {
    private static final String EJSID = "756b423a0a6fe6a7";
    private static final String SID = "1684bb4a0f94302c";

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

    public void testPostingOfSolution() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        if (posted.get() == null) {
            fail("There is no post request.");
            return;
        }
        assertTrue(posted.get().startsWith("POST /ejudge"));
    }

    public void testCookieInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var request = posted.get();
        assertTrue(
            request.contains("Cookie:")
                && request.contains("EJSID=" + EJSID)
        );
    }

    public void testContentTypeInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var request = posted.get();
        assertTrue(
            request.contains("Content-Type: multipart/form-data")
                && request.contains("boundary=")
        );
    }

    public void testSidInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var body = posted.get();
        assertTrue(
            body.contains("name=\"SID\"")
                && body.contains(SID)
        );
    }

    public void testProbIdInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var body = posted.get();
        assertTrue(
            body.contains("name=\"prob_id\"")
                && body.contains("3")
        );
    }

    public void testLangIdInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var body = posted.get();
        assertTrue(
            body.contains("name=\"lang_id\"")
                && body.contains("82")
        );
    }

    public void testActionInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var body = posted.get();
        assertTrue(
            body.contains("name=\"action_40\"")
                && body.contains("Send!")
        );
    }

    public void testFilePartInPost() {
        final var posted = new AtomicReference<String>();
        new SilentSubmit(
            this.file(),
            this.form(this.driver(posted)),
            this.resource(this.driver(posted))
        ).run();
        final var body = posted.get();
        assertTrue(
            body.contains("filename=\"problem\"")
                && body.contains("int main() {}")
        );
    }

    public void testUnexpectedStatus() {
        try {
            final var posted = new AtomicReference<String>();
            final var driver = this.unexpectedStatusDriver(posted);
            new SilentSubmit(
                this.file(),
                this.form(driver),
                this.resource(driver)
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testUnknownProblem() {
        final var posted = new AtomicReference<String>();
        try {
            new SilentSubmit(
                this.file("// problem: NonexistentProblem\n"),
                this.form(this.driver(posted)),
                this.resource(this.driver(posted))
            ).run();
        } catch (final InvariantViolation e) {
            assertNull(posted.get());
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        final var posted = new AtomicReference<String>();
        try {
            final WebDriver driver = (loc, req) -> {
                throw new InvariantViolation("There is no resource.");
            };
            new SilentSubmit(
                this.file(),
                this.form(driver),
                this.resource(driver)
            ).run();
        } catch (final InvariantViolation e) {
            assertNull(posted.get());
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenFile() {
        final var posted = new AtomicReference<String>();
        try {
            new SilentSubmit(
                new ByteFile.Of(
                    new Text.Of("broken"),
                    () -> {
                        throw new InvariantViolation("There is no contents.");
                    }
                ),
                this.form(this.driver(posted)),
                this.resource(this.driver(posted))
            ).run();
        } catch (final InvariantViolation e) {
            assertNull(posted.get());
            return;
        }
        fail("InvariantViolation");
    }

    private WebDriver driver(final AtomicReference<String> posted) {
        return (loc, req) -> {
            final var request = new String(req.bytes(), StandardCharsets.UTF_8);
            if (request.startsWith("POST")) {
                posted.set(request);
                return this.accepted();
            }
            if (request.contains("prob_id=")) {
                return this.problem.getBytes(StandardCharsets.UTF_8);
            }
            return this.main.getBytes(StandardCharsets.UTF_8);
        };
    }

    private WebDriver unexpectedStatusDriver(final AtomicReference<String> posted) {
        return (loc, req) -> {
            final var request = new String(req.bytes(), StandardCharsets.UTF_8);
            if (request.startsWith("POST")) {
                posted.set(request);
                return """
                    HTTP/1.1 200 OK\r
                    Content-Length: 0\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8);
            }
            if (request.contains("prob_id=")) {
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

    private ByteFile file() {
        return this.file("// problem: WithLinks\n// language: 2\nint main() {}");
    }

    private ByteFile file(final String content) {
        return new ByteFile.Of(
            new Text.Of("problem"),
            new Bytes.Of(content.getBytes(StandardCharsets.UTF_8))
        );
    }

    private ContestForm form(final WebDriver driver) {
        return new ContestForm(driver, this.location(), this.session());
    }

    private ContestResource resource(final WebDriver driver) {
        return new ContestResource(driver, this.location(), this.session());
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
