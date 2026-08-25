package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class ChildrenOf implements DocPath {
    private final Text src;

    public ChildrenOf(final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s/*"),
            new TextOfPath(p)
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
