package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class UrlText implements Text {
    private final Text origin;

    public UrlText(final Text origin) {
        this.origin = origin;
    }

    @Override
    public String content() throws InvariantViolation {
        return URLEncoder.encode(
            this.origin.content(),
            StandardCharsets.UTF_8
        ).replace("+", "%20");
    }
}
