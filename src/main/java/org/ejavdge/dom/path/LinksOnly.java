package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class LinksOnly implements DocPath {
    private final Text src;

    public LinksOnly(final DocPath p) {
        this(new Text.Of("\n"), p);
    }

    public LinksOnly(final Text t, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("string-join(%s//@href, '%s')"),
            new TextOfPath(
                new OnlyTag("a", p)
            ),
            t
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
