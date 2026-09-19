package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class WithText implements DocPath {
    private final Text src;

    public WithText(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public WithText(final Text t, final DocPath p) {
        this(
            new Stencil(
                new Text.Of("%s[text() = '%s']"),
                new TextOfPath(p),
                t
            )
        );
    }

    public WithText(final Text t) {
        this.src = t;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
