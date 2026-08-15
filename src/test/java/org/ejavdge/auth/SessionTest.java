package org.ejavdge.auth;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;

import java.nio.charset.StandardCharsets;

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

    public void testInvalidCredentials() {
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
}
