package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class ContentBased implements Text {
    private final Text origin;

    public ContentBased(final Text t) {
        this.origin = t;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ContentBased t) {
            return this.content().equals(t.content());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.content().hashCode();
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}