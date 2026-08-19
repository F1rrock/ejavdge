package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.NonNegative;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

public final class RunId implements Context {
    private final Num src;

    public RunId(final int src) {
        this(new Num.Of(src));
    }

    public RunId(final Num src) {
        this.src = new NumAbout(
            "run id",
            new NonNegative(src)
        );
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("run_id"), new TextOfNum(this.src))
            .content();
    }
}
