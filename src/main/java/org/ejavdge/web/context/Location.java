package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.media.Media;
import org.ejavdge.web.media.WhiteList;
import org.ejavdge.web.resource.Url;

public final class Location implements Context {
    private final Url url;
    private final Text host;
    private final Num port;

    public Location(final Location loc, final Context query) {
        this.url = new Url(loc.url, query);
        this.host = loc.host;
        this.port = loc.port;
    }

    public Location(final Text base, final Text host, final Num port) {
        this.url = new Url(base);
        this.host = new TextAbout("host", host);
        this.port = new NumAbout("port", new Positive(port));
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("url"), this.url)
            .with(new Text.Of("host"), this.host)
            .with(new Text.Of("port"), new TextOfNum(this.port))
            .content();
    }

    public static final class Host implements Context {
        private final Location loc;

        public Host(final Location loc) {
            this.loc = loc;
        }

        @Override
        public <T> T imprint(final Media<T> m) throws InvariantViolation {
            return this.loc.imprint(
                new WhiteList<>(
                    new Text.Of("host"),
                    m
                )
            );
        }
    }

    public static final class Port implements Context {
        private final Location loc;

        public Port(final Location loc) {
            this.loc = loc;
        }

        @Override
        public <T> T imprint(final Media<T> m) throws InvariantViolation {
            return this.loc.imprint(
                new WhiteList<>(
                    new Text.Of("port"),
                    m
                )
            );
        }
    }
}
