package org.ejavdge.auth;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Verbose;
import org.ejavdge.scalar.bytes.WithRetry;
import org.ejavdge.scalar.bytes.WithTimeout;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class SessionTest extends TestCase {
    public void testCachedSessionMakesNoFurtherRequests() {
        final AtomicInteger requests = new AtomicInteger();
        final Session session = new Session(
            (location, request) -> {
                requests.incrementAndGet();
                return this.ok();
            },
            this.location(),
            this.credentials("login", "pass")
        );
        session.content();
        session.content();
        assertEquals(1, requests.get());
    }

    public void testCachedSessionDoesNotLogAgain() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PrintStream original = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        try {
            final Session session = new Session(
                this.successfulDriver(),
                this.location(),
                this.credentials("login", "pass")
            );
            session.content();
            output.reset();
            session.content();
        } finally {
            System.setOut(original);
        }
        assertEquals(0, this.countLogs(output));
    }

    public void testSuccessfulSessionMakesOneNetworkAttempt() {
        final AtomicInteger requests = new AtomicInteger();
        new Session(
            (location, request) -> {
                requests.incrementAndGet();
                return this.ok();
            },
            this.location(),
            this.credentials("login", "pass")
        ).content();
        assertEquals(1, requests.get());
    }

    public void testSuccessfulSessionLogsOnce() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PrintStream original = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        try {
            new Session(
                this.successfulDriver(),
                this.location(),
                this.credentials("login", "pass")
            ).content();
        } finally {
            System.setOut(original);
        }
        assertEquals(1, this.countLogs(output));
    }

    public void testRetryAfterFailedAttemptMakesTwoNetworkAttempts() {
        final AtomicInteger requests = new AtomicInteger();
        new Session(
            (location, request) -> {
                if (requests.incrementAndGet() == 1) {
                    return this.unauthorized();
                }
                return this.ok();
            },
            this.location(),
            this.credentials("login", "pass")
        ).content();
        assertEquals(2, requests.get());
    }

    public void testRetryAfterFailedAttemptLogsTwice() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PrintStream original = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        try {
            final AtomicInteger requests = new AtomicInteger();
            new Session(
                (location, request) -> {
                    if (requests.incrementAndGet() == 1) {
                        return this.unauthorized();
                    }
                    return this.ok();
                },
                this.location(),
                this.credentials("login", "pass")
            ).content();
        } finally {
            System.setOut(original);
        }
        assertEquals(2, this.countLogs(output));
    }

    public void testFiveFailedAttemptsMakeFiveNetworkAttempts() {
        final AtomicInteger requests = new AtomicInteger();
        this.failure(
            new Session(
                (location, request) -> {
                    requests.incrementAndGet();
                    return this.unauthorized();
                },
                this.location(),
                this.credentials("login", "pass")
            )
        );
        assertEquals(5, requests.get());
    }

    public void testFiveFailedAttemptsLogFiveTimes() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PrintStream original = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        try {
            this.failure(
                new Session(
                    this.failingDriver(),
                    this.location(),
                    this.credentials("login", "pass")
                )
            );
        } finally {
            System.setOut(original);
        }
        assertEquals(5, this.countLogs(output));
    }

    public void testTimeoutOfAttemptCausesRetry() {
        final AtomicInteger calls = new AtomicInteger();
        final Bytes origin = () -> {
            if (calls.incrementAndGet() == 1) {
                this.sleep();
                throw new InvariantViolation("timeout");
            }
            return this.ok();
        };
        new Session(
            new WithRetry(
                new Verbose(
                    new WithTimeout(origin, Duration.ofMillis(20)),
                    new Text.Of("Fetching session..."),
                    this.logger(new AtomicInteger())
                ),
                new Num.Of(2)
            )
        ).content();
        assertEquals(2, calls.get());
    }

    public void testSuccessfulRetryAfterTimeoutReturnsContent() {
        final AtomicInteger calls = new AtomicInteger();
        final Bytes origin = () -> {
            if (calls.incrementAndGet() == 1) {
                this.sleep();
                throw new InvariantViolation("timeout");
            }
            return this.ok();
        };
        assertTrue(
            this.text(
                new Session(
                    new WithRetry(
                        new Verbose(
                            new WithTimeout(origin, Duration.ofMillis(20)),
                            new Text.Of("Fetching session..."),
                            this.logger(new AtomicInteger())
                        ),
                        new Num.Of(5)
                    )
                ).content()
            ).contains("OK")
        );
    }

    public void testSuccessfulRetryAfterTimeoutStopsFurtherAttempts() {
        final AtomicInteger calls = new AtomicInteger();
        final Bytes origin = () -> {
            if (calls.incrementAndGet() == 1) {
                this.sleep();
                throw new InvariantViolation("timeout");
            }
            return this.ok();
        };
        new Session(
            new WithRetry(
                new Verbose(
                    new WithTimeout(origin, Duration.ofMillis(20)),
                    new Text.Of("Fetching session..."),
                    this.logger(new AtomicInteger())
                ),
                new Num.Of(5)
            )
        ).content();
        assertEquals(2, calls.get());
    }

    private InvariantViolation failure(final Bytes origin) {
        try {
            origin.content();
        } catch (final InvariantViolation err) {
            return err;
        }
        return null;
    }

    private WebDriver successfulDriver() {
        return (location, request) -> this.ok();
    }

    private WebDriver failingDriver() {
        return (location, request) -> this.unauthorized();
    }

    private String text(final byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private long countLogs(final ByteArrayOutputStream output) {
        return output
            .toString(StandardCharsets.UTF_8)
            .lines()
            .filter(line -> line.contains("Fetching session..."))
            .count();
    }

    private Logger logger(final AtomicInteger calls) {
        return (Logger) Proxy.newProxyInstance(
            Logger.class.getClassLoader(),
            new Class<?>[]{Logger.class},
            (proxy, method, args) -> {
                if (method.getName().equals("isDebugEnabled")) {
                    return true;
                }
                if (method.getName().equals("debug")) {
                    calls.incrementAndGet();
                }
                return null;
            }
        );
    }

    private Location location() {
        return new Location(
            new Text.Of("/ejudge"),
            new Text.Of("0.0.0.0"),
            new Num.Of(90)
        );
    }

    private Credentials credentials(final String login, final String password) {
        return new Credentials(
            new Text.Of(login),
            new Text.Of(password),
            new Num.Of(1)
        );
    }

    private byte[] ok() {
        return """
            HTTP/1.1 302 Continue\r
            Content-Length: 2\r
            \r
            OK
            """.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] unauthorized() {
        return """
            HTTP/1.1 401 Unauthorized\r
            Content-Length: 6\r
            \r
            Failed
            """.getBytes(StandardCharsets.UTF_8);
    }

    private void sleep() {
        try {
            Thread.sleep(200L);
        } catch (final InterruptedException err) {
            Thread.currentThread().interrupt();
        }
    }
}
