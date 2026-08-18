package org.ejavdge.auth;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class SessionTest extends TestCase {
    public void testCorrectCredentials() {
        assertTrue(
            new String(
                new Session(
                    new FakeDriver("login", "pass"),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("0.0.0.0"),
                        new Num.Of(90)
                    ),
                    new Credentials(
                        new Text.Of("login"),
                        new Text.Of("pass"),
                        new Num.Of(1)
                    )
                ).content(),
                StandardCharsets.UTF_8
            ).contains("OK")
        );
    }

    public void testIncorrectCredentials() {
        try {
            new Session(
                new FakeDriver("login", "pass"),
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("0.0.0.0"),
                    new Num.Of(90)
                ),
                new Credentials(
                    new Text.Of("wrong"),
                    new Text.Of("pass"),
                    new Num.Of(1)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testFiveFailedAttempts() {
        final var calls = new AtomicInteger();
        try {
            new Session(
                (loc, req) -> {
                    calls.incrementAndGet();
                    throw new InvariantViolation("there is no content");
                },
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("0.0.0.0"),
                    new Num.Of(90)
                ),
                new Credentials(
                    new Text.Of("login"),
                    new Text.Of("pass"),
                    new Num.Of(1)
                )
            ).content();
        } catch (final InvariantViolation e) {
            assertEquals(5, calls.get());
        }
    }

    public void testRetry() {
        final var calls = new AtomicInteger();
        final var d = new FakeDriver("login", "pass");
        new Session(
            (loc, req) -> {
                if (calls.incrementAndGet() < 3) {
                    throw new InvariantViolation("there is no content");
                }
                return d.resourceOf(loc, req);
            },
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("0.0.0.0"),
                new Num.Of(90)
            ),
            new Credentials(
                new Text.Of("login"),
                new Text.Of("pass"),
                new Num.Of(1)
            )
        ).content();
        assertEquals(3, calls.get());
    }

    public void testCache() {
        final var calls = new AtomicInteger();
        final var d = new FakeDriver("login", "pass");
        final var s = new Session(
            (loc, req) -> {
                calls.incrementAndGet();
                return d.resourceOf(loc, req);
            },
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("0.0.0.0"),
                new Num.Of(90)
            ),
            new Credentials(
                new Text.Of("login"),
                new Text.Of("pass"),
                new Num.Of(1)
            )
        );
        s.content();
        s.content();
        assertEquals(1, calls.get());
    }
}
