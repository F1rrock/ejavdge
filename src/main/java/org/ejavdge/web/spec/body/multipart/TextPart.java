package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.*;

public final class TextPart implements Part {
    private final Bytes src;

    public TextPart(final Text n, final Text v) {
        this(
            new Utf8(
                new Concat(
                    new Stencil(
                        new Text.Of(
                            """
                            Content-Disposition: form-data; name="%s"\r
                            \r
                            """
                        ),
                        new TextAbout(
                            "text variable name",
                            new NonEmpty(n)
                        )
                    ),
                    v
                )
            )
        );
    }

    public TextPart(final Bytes s) {
        this.src = s;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
