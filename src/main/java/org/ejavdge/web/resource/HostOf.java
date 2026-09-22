package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

import java.net.URI;

public final class HostOf implements Text {
    private final Url url;

    public HostOf(final Text u) {
        this(new Url(u));
    }

    public HostOf(final Url u) {
        this.url = u;
    }

    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "host of url",
            () -> {
                try {
                    final var host =  URI.create(this.url.content()).getHost();
                    if (host == null) {
                        throw new InvariantViolation("There is no host in url");
                    }
                    return host;
                } catch (final IllegalArgumentException e) {
                    throw new InvariantViolation("There is no legal url", e);
                }
            }
        ).content();
    }
}
