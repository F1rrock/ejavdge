package org.ejavdge.domain.tokens;

import org.ejavdge.auth.LoginReply;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;

public final class Ejsid implements Text {
    private final Text origin;

    public Ejsid(final WebDriver d, final Location l, final Credentials c) {
        this(
            new Match(
                new Utf8Text(
                    new LoginReply(d, l, c)
                ),
                new Text.Of("(?<=EJSID=)[^;]+")
            )
        );
    }

    public Ejsid(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
