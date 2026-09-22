package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.text.Text;

import java.net.URI;

public final class PortOf implements Num {
    private final Url url;

    public PortOf(final Text u) {
        this(new Url(u));
    }

    public PortOf(final Url u) {
        this.url = u;
    }

    @Override
    public int value() throws InvariantViolation {
        return new NumAbout(
            "port of url",
            () -> {
                try {
                    final var uri = URI.create(this.url.content());
                    final var p = uri.getPort();
                    if (p >= 0) {
                        return p;
                    }
                    return "https".equals(uri.getScheme()) ? 443 : 80;
                } catch (final IllegalArgumentException e) {
                    throw new InvariantViolation("There is no legal url", e);
                }
            }
        ).value();
    }
}
