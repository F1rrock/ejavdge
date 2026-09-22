package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.*;

import java.net.URI;

public final class PathOf implements Text {
    private final Url url;

    public PathOf(final Text u) {
        this(new Url(u));
    }

    public PathOf(final Url u) {
        this.url = u;
    }

    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "path of url",
            new BindOfText(
                () -> {
                    try {
                        final var path =  URI.create(this.url.content()).getPath();
                        if (path == null) {
                            throw new InvariantViolation("There is no path in url");
                        }
                        return path;
                    } catch (final IllegalArgumentException e) {
                        throw new InvariantViolation("There is no legal url", e);
                    }
                },
                p -> new Fallback(
                    new NonEmpty(new Text.Of(p)),
                    new Text.Of("/")
                )
            )
        ).content();
    }
}
