package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class BeforeId implements DocPath {
    private final Text src;

    public BeforeId(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public BeforeId(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[following-sibling::*[@id = '%s']]"),
            new TextOfPath(p),
            new NonEmpty(t)
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
