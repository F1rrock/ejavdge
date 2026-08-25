package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class AllNodes implements DocPath {
    private final Text src;

    public AllNodes() {
        this(new Text.Of("//*"));
    }

    public AllNodes(final Text t) {
        this.src = t;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
