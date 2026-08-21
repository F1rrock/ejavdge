package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Uuid;

public final class Boundary implements Bytes {
    private final Bytes origin;

    public Boundary() {
        this(new Uuid());
    }

    public Boundary(final Text id) {
        this(
            new Utf8(
                new Stencil(
                    new Text.Of("----WebKitFormBoundary%s"),
                    id
                )
            )
        );
    }

    public Boundary(final Bytes bs) {
        this.origin = bs;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
