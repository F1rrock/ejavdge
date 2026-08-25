package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class TextOfPath implements Text {
    private final DocPath src;

    public TextOfPath(final DocPath p) {
        this.src = p;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.src.view();
    }
}
