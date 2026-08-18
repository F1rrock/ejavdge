package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

public final class ProbId implements Context {
    private final Num src;

    public ProbId(final int n) {
        this(new Num.Of(n));
    }

    public ProbId(final Num n) {
        this.src = new NumAbout(
            "problem id",
            new Positive(n)
        );
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("prob_id"), new TextOfNum(this.src))
            .content();
    }
}
