package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Ascii;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

public final class Terminator implements Bytes {
    private final Bytes origin;

    public Terminator() {
        this.origin = new Ascii(
            new Text.Of("\r\n")
        );
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
