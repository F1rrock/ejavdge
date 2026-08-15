package org.ejavdge.auth;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Retry;
import org.ejavdge.scalar.bytes.Timeout;
import org.ejavdge.scalar.bytes.Verbose;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class SessionTest extends TestCase {

    public void testCachedSessionNoNetworkAndNoLogging() {
        final AtomicInteger requests = new AtomicInteger();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final PrintStream original = System.out;

        System.setOut(
            new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
            )
        );

        try {
            final WebDriver driver = (location, request) -> {
                requests.incrementAndGet();
                return ok();
            };

            final Session session = new Session(
                driver,
                location(),
                credentials("login", "pass")
            );

            assertTrue(
                text(session.content()).contains("OK")
            );

            output.reset();

            assertTrue(
                text(session.content()).contains("OK")
            );

            assertEquals(1, requests.get());
            assertEquals(
                "",
                output.toString(StandardCharsets.UTF_8)
            );
        } finally {
            System.setOut(original);
        }
    }

    public void testSuccessfulNetworkRetrievalOneRequestOneLog() {
        final AtomicInteger requests = new AtomicInteger();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final PrintStream original = System.out;

        System.setOut(
            new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
            )
        );

        try {
            final WebDriver driver = (location, request) -> {
                requests.incrementAndGet();
                return ok();
            };

            final Session session = new Session(
                driver,
                location(),
                credentials("login", "pass")
            );

            assertTrue(
                text(session.content()).contains("OK")
            );

            assertEquals(1, requests.get());

            assertEquals(
                "Fetching session..." + System.lineSeparator(),
                output.toString(StandardCharsets.UTF_8)
            );
        } finally {
            System.setOut(original);
        }
    }

    public void testFailedAttemptThenSuccessfulRetry() {
        final AtomicInteger requests = new AtomicInteger();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final PrintStream original = System.out;

        System.setOut(
            new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
            )
        );

        try {
            final WebDriver driver = (location, request) -> {
                if (requests.incrementAndGet() == 1) {
                    return unauthorized();
                }

                return ok();
            };

            final Session session = new Session(
                driver,
                location(),
                credentials("login", "pass")
            );

            assertTrue(
                text(session.content()).contains("OK")
            );

            assertEquals(2, requests.get());
            assertEquals(2, countLogs(output));
        } finally {
            System.setOut(original);
        }
    }

    public void testFiveFailedAttempts() {
        final AtomicInteger requests = new AtomicInteger();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final PrintStream original = System.out;

        System.setOut(
            new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
            )
        );

        try {
            final WebDriver driver = (location, request) -> {
                requests.incrementAndGet();
                return unauthorized();
            };

            try {
                new Session(
                    driver,
                    location(),
                    credentials("login", "pass")
                ).content();
            } catch (final InvariantViolation err) {
                assertEquals(5, requests.get());
                assertEquals(5, countLogs(output));
                return;
            }
        } finally {
            System.setOut(original);
        }

        fail("InvariantViolation");
    }

    public void testTimeoutCausesRetry() {
        final AtomicInteger calls = new AtomicInteger();

        final Bytes origin = () -> {
            if (calls.incrementAndGet() == 1) {
                try {
                    Thread.sleep(200L);
                } catch (final InterruptedException err) {
                    Thread.currentThread().interrupt();
                }

                throw new InvariantViolation("timeout");
            }

            return "OK".getBytes(StandardCharsets.UTF_8);
        };

        final Session session = new Session(
            new Retry(
                new Verbose(
                    new Timeout(
                        origin,
                        Duration.ofMillis(20)
                    ),
                    "Fetching session..."
                ),
                2
            )
        );

        assertEquals(
            "OK",
            text(session.content())
        );

        assertEquals(2, calls.get());
    }

    public void testSuccessfulRetryAfterTimeoutStopsFurtherAttempts() {
        final AtomicInteger calls = new AtomicInteger();

        final Bytes origin = () -> {
            if (calls.incrementAndGet() == 1) {
                try {
                    Thread.sleep(200L);
                } catch (final InterruptedException err) {
                    Thread.currentThread().interrupt();
                }

                throw new InvariantViolation("timeout");
            }

            return "OK".getBytes(StandardCharsets.UTF_8);
        };

        final Session session = new Session(
            new Retry(
                new Verbose(
                    new Timeout(
                        origin,
                        Duration.ofMillis(20)
                    ),
                    "Fetching session..."
                ),
                5
            )
        );

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final PrintStream original = System.out;

        System.setOut(
            new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
            )
        );

        try {
            assertEquals(
                "OK",
                text(session.content())
            );

            assertEquals(2, calls.get());
            assertEquals(2, countLogs(output));
        } finally {
            System.setOut(original);
        }
    }

    public void testCorrectCredentials() {
        assertTrue(
            text(
                new Session(
                    new FakeDriver("login", "pass"),
                    location(),
                    credentials("login", "pass")
                ).content()
            ).contains("OK")
        );
    }

    public void testInvalidCredentials() {
        try {
            new Session(
                new FakeDriver("login", "pass"),
                location(),
                credentials("wrong", "pass")
            ).content();
        } catch (final InvariantViolation err) {
            return;
        }

        fail("InvariantViolation");
    }

    private static String text(final byte[] bytes) {
        return new String(
            bytes,
            StandardCharsets.UTF_8
        );
    }

    private static long countLogs(final ByteArrayOutputStream output) {
        return output
            .toString(StandardCharsets.UTF_8)
            .lines()
            .filter("Fetching session..."::equals)
            .count();
    }

    private static Location location() {
        return new Location(
            new Text.Of("/ejudge"),
            new Text.Of("0.0.0.0"),
            new Num.Of(90)
        );
    }

    private static Credentials credentials(final String login, final String password) {
        return new Credentials(
            new Text.Of(login),
            new Text.Of(password),
            new Num.Of(1)
        );
    }

    private static byte[] ok() {
        return """
            HTTP/1.1 302 Found\r
            Content-Length: 2\r
            \r
            OK
            """.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] unauthorized() {
        return """
            HTTP/1.1 401 Unauthorized\r
            Content-Length: 6\r
            \r
            Failed
            """.getBytes(StandardCharsets.UTF_8);
    }
}