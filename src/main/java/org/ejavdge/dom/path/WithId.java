package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class WithId implements DocPath {
    private final Text src;

    public WithId(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public WithId(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[@id = '%s']"),
            new TextOfPath(p),
            t
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
