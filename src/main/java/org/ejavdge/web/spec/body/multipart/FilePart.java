package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

public final class FilePart implements Part {
    private final Bytes src;

    public FilePart(final Text n, final Bytes bs) {
        this(
            new Concat(
                new Utf8(
                    new Stencil(
                        new Text.Of(
                            """
                            Content-Disposition: form-data; name="file"; filename="%s"\r
                            Content-Type: application/octet-stream\r
                            \r
                            """
                        ),
                        new TextAbout(
                            "file name",
                            new NonEmpty(n)
                        )
                    )
                ),
                bs
            )
        );
    }

    public FilePart(final Bytes src) {
        this.src = src;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
