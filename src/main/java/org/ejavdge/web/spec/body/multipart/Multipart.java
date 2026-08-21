package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Uuid;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Terminator;
import org.ejavdge.web.spec.body.WithBody;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;

public final class Multipart implements HttpSpec {
    private final Bytes src;

    public Multipart(final Items<Part> ps, final HttpSpec hs) {
        this(
            ps,
            new Stencil(
                new Text.Of("----WebKitFormBoundary%s"),
                new Uuid()
            ),
            hs
        );
    }

    public Multipart(final Items<Part> ps, final Text br, final HttpSpec hs) {
        this(
            new ByteView(
                new WithBody(
                    new BindOfBytes(
                        new Concat(
                            new Utf8(new Text.Of("--")),
                            new Utf8(br)
                        ),
                        pref -> new Concat(
                            new Bytes.Of(pref),
                            new Concat(
                                new Map<>(
                                    p -> new Concat(
                                        new Terminator(),
                                        new BytesOfPart(p),
                                        new Terminator(),
                                        new Bytes.Of(pref)
                                    ),
                                    ps
                                )
                            ),
                            new Utf8(new Text.Of("--"))
                        )
                    ),
                    new WithHeaders(
                        new Header(
                            new Text.Of("Content-Type"),
                            new Stencil(
                                new Text.Of("multipart/form-data; boundary=%s"),
                                br
                            )
                        ),
                        hs
                    )
                )
            )
        );
    }

    public Multipart(final Bytes bs) {
        this.src = bs;
    }

    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
