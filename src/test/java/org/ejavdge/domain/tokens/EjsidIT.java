package org.ejavdge.domain.tokens;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;

public final class EjsidIT extends TestCase {
    public void testCorrectEjsid() {
        assertEquals(
            "306024e6e473944c",
            new Ejsid(
                new Session(
                    new FakeDriver(),
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
                )
            ).content()
        );
    }

    public void testIncorrectEjsid() {
        try {
            new Ejsid(
                new Session(
                    new FakeDriver(),
                    new Location(
                        new Text.Of("/wrong"),
                        new Text.Of("0.0.0.0"),
                        new Num.Of(90)
                    ),
                    new Credentials(
                        new Text.Of("login"),
                        new Text.Of("pass"),
                        new Num.Of(1)
                    )
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
