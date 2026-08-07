package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;
import org.ejavdge.web.media.WhiteList;

public final class Location implements Context {
    private final Text url;
    private final Text host;
    private final Num port;

    public Location(final String u, final String h, final int p) {
        this(new Text.Of(u), new Text.Of(h), new Num.Of(p));
    }

    public Location(final Text u, final Text h, final Num p) {
        this.url = new TextAbout("url", u);
        this.host = new TextAbout("host", h);
        this.port = new NumAbout("port", new Positive(p));
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
