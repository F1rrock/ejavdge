package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Terminator;
import org.ejavdge.web.spec.body.WithBody;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;

public final class Multipart implements HttpSpec {
    private final Bytes src;

    public Multipart(final Items<Part> ps, final HttpSpec hs) {
        this(ps, new Boundary(), hs);
    }

    public Multipart(final Items<Part> ps, final Boundary br, final HttpSpec hs) {
        this(
            new ByteView(
                new WithBody(
                    new BindOfBytes(
                        new WithPrefix(br),
                        pref -> new Concat(
                            new Concat(
                                new Map<>(
                                    p -> new Concat(
                                        new Bytes.Of(pref),
                                        new Terminator(),
                                        new BytesOfPart(p),
                                        new Terminator()
                                    ),
                                    ps
                                )
                            ),
                            new WithSuffix(pref)
                        )
                    ),
                    new WithHeaders(
                        new Header(
                            new Text.Of("Content-Type"),
                            new Stencil(
                                new Text.Of("multipart/form-data; boundary=%s"),
                                new Utf8Text(br)
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
