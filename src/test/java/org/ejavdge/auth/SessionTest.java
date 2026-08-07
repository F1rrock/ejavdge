package org.ejavdge.auth;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;

import java.nio.charset.StandardCharsets;

public final class SessionTest extends TestCase {
    public void testSession() {
        try (final var ejudge = new FakeEjudge()) {
            ejudge.start();
            final var session = new Session(
                new JdkSocket(),
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("localhost"),
                    new Num.Of(ejudge.port())
                ),
                new Credentials(
                    new Text.Of("login"),
                    new Text.Of("pass"),
                    new Num.Of(1)
                )
            );
            final var content = new String(
                session.content(),
                StandardCharsets.UTF_8
            );
            assertTrue(content.startsWith("HTTP/1.1 302"));
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    public void testInvalidCredentials() {
        try (final var ejudge = new FakeEjudge()) {
            ejudge.start();
            final var session = new Session(
                new JdkSocket(),
                new Location(
                    new Text.Of("/ejudge"),
                    new Text.Of("localhost"),
                    new Num.Of(ejudge.port())
                ),
                new Credentials(
                    new Text.Of("wrong"),
                    new Text.Of("pass"),
                    new Num.Of(1)
                )
            );
            try {
                session.content();
            } catch (final InvariantViolation e) {
                return;
            }
            fail("InvariantViolation");
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    public void testConnectionRefused() {
        final var session = new Session(
            new JdkSocket(),
            new Location(
                new Text.Of("/ejudge"),
                new Text.Of("localhost"),
                new Num.Of(9999)
            ),
            new Credentials(
                new Text.Of("login"),
                new Text.Of("pass"),
                new Num.Of(1)
            )
        );
        try {
            session.content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
