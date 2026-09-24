package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class ValuesOnly implements DocPath {
    private final Text src;

    public ValuesOnly(final DocPath p) {
        this(new Text.Of("\n"), p);
    }

    public ValuesOnly(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("string-join(%s//@value, '%s')"),
            new TextOfPath(p),
            t
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
