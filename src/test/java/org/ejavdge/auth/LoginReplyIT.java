package org.ejavdge.auth;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;

import java.nio.charset.StandardCharsets;

public final class LoginReplyIT extends TestCase {
    public void testCorrectCredentials() {
        try (final var ejudge = new FakeEjudge()) {
            ejudge.start();
            final var actual = new String(
                new LoginReply(
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
                ).content(),
                StandardCharsets.US_ASCII
            );
            assertTrue(actual.startsWith("HTTP/1.1 200"));
            assertTrue(
                actual.endsWith("Welcome to ejudge!")
            );
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    public void testIncorrectCredentials() {
        try (final var ejudge = new FakeEjudge()) {
            ejudge.start();
            final var actual = new String(
                new LoginReply(
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
                ).content(),
                StandardCharsets.US_ASCII
            );
            assertTrue(actual.startsWith("HTTP/1.1 403"));
            assertTrue(
                actual.endsWith("Invalid credentials")
            );
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
}
