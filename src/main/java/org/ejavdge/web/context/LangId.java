package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

public final class LangId implements Context {
    private final Context origin;

    public LangId(final int n) {
        this(new Num.Of(n));
    }

    public LangId(final Num n) {
        this(
            new WithEntry(
                new Text.Of("lang_id"),
                new TextAbout(
                    "language id",
                    new TextOfNum(
                        new Positive(n)
                    )
                )
            )
        );
    }

    public LangId(final Context c) {
        this.origin = c;
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}
