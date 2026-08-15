package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;

public final class Terminator implements Bytes {
    private final Bytes origin;

    public Terminator() {
        this.origin = new Utf8(
            new Text.Of("\r\n")
        );
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
