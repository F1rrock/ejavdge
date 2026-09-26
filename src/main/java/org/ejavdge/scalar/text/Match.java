package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.regex.Pattern;

public final class Match implements Text {
    private final Text origin;
    private final Text regex;
    private final Text message;

    public Match(final Text t, final Text r) {
        this(
            t, r,
            new Concat(
                new Text.Of("Text does not match regex: "),
                r
            )
        );
    }

    public Match(final Text t, final Text r, final Text m) {
        this.origin = t;
        this.regex = r;
        this.message = m;
    }

    @Override
    public String content() throws InvariantViolation {
        final var r = this.regex.content();
        final var matcher = Pattern
            .compile(r)
            .matcher(this.origin.content());
        if (!matcher.find()) {
            throw new InvariantViolation(
                this.message.content()
            );
        }
        return matcher.group();
    }
}
