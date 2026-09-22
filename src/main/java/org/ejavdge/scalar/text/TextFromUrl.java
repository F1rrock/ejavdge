package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class TextFromUrl implements Text {
    private final Text origin;
    private final Text message;

    public TextFromUrl(final Text u) {
        this(u, new Text.Of("There is no legal url."));
    }

    public TextFromUrl(final Text u, final Text m) {
        this.origin = u;
        this.message = m;
    }

    @Override
    public String content() throws InvariantViolation {
        try {
            return URLDecoder.decode(
                this.origin.content(),
                StandardCharsets.UTF_8
            );
        } catch (final IllegalArgumentException e) {
            throw new InvariantViolation(this.message.content(), e);
        }
    }
}
