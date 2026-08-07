package org.ejavdge.domain.tokens;

import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Utf8Text;

public final class Ejsid implements Text {
    private final Text origin;

    public Ejsid(final Session s) {
        this(
            new TextAbout(
                "ejsid",
                new Match(
                    new Utf8Text(s),
                    new Text.Of("(?<=EJSID=)[^;]+")
                )
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
