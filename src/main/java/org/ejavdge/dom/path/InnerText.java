package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class InnerText implements DocPath {
    private final Text src;

    public InnerText(final DocPath p) {
        this(new Text.Of("\n"), p);
    }

    public InnerText(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("string-join(%s/text(), '%s')"),
            new TextOfPath(p),
            t
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
