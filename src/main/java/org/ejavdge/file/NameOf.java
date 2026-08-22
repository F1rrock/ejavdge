package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class NameOf implements Text {
    private final ByteFile src;

    public NameOf(final ByteFile f) {
        this.src = f;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.src.name();
    }
}
