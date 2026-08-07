package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

public final class Credentials implements Context {
    private final Text login;
    private final Text pass;
    private final Num contest;

    public Credentials(final String l, final String p, final int c) {
        this(new Text.Of(l), new Text.Of(p), new Num.Of(c));
    }

    public Credentials(final Text l, final Text p, final Num c) {
        this.login = new TextAbout("login", l);
        this.pass = new TextAbout("password", p);
        this.contest = new NumAbout("contest id", new Positive(c));
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("login"), this.login)
            .with(new Text.Of("password"), this.pass)
            .with(new Text.Of("contest_id"), new TextOfNum(this.contest))
            .content();
    }
}
