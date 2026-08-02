package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.regex.Pattern;

public final class Match implements Text {
    private final Text origin;
    private final Text regex;

    public Match(final Text origin, final Text regex) {
        this.origin = origin;
        this.regex = regex;
    }

    @Override
    public String content() throws InvariantViolation {
        final var r = this.regex.content();
        final var matcher = Pattern
            .compile(r)
            .matcher(this.origin.content());
        if (!matcher.find()) {
            throw new InvariantViolation(
                "Text does not match regex: " + r
            );
        }
        return matcher.group();
    }
}
