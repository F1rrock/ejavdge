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

/**
 * An {@link HttpSpec} that encodes a list of {@link Part}s as a
 * {@code multipart/form-data} request body.
 * <p>
 * A multipart body consists of a boundary marker followed by one or more
 * parts, each introduced by the boundary and terminated by a line break,
 * and a closing boundary at the end. This class assembles the body from
 * the supplied parts, using a {@link Boundary} as the delimiter, and sets
 * the {@code Content-Type} header to
 * {@code multipart/form-data; boundary=<value>} where {@code <value>} is
 * the textual form of the boundary.
 * <p>
 * The boundary can be supplied explicitly, or a fresh random one can be
 * generated automatically. In either case, the same boundary is used for
 * the header and for all part delimiters, and the resulting HTTP message
 * is exposed as raw bytes via {@link #bytes()}.
 */
public final class Multipart implements HttpSpec {

    /**
     * The underlying byte content of the multipart message.
     */
    private final Bytes src;

    /**
     * Creates a multipart body from the given parts, using a freshly
     * generated random boundary and the given surrounding HTTP spec.
     *
     * @param ps the parts to include in the body
     * @param hs the surrounding HTTP spec that will wrap the body
     */
    public Multipart(final Items<Part> ps, final HttpSpec hs) {
        this(ps, new Boundary(), hs);
    }

    /**
     * Creates a multipart body from the given parts, using the given
     * boundary and the given surrounding HTTP spec.
     * <p>
     * Each part is prefixed with the boundary and a terminator, and
     * followed by a terminator. The body is closed with the closing
     * boundary. The {@code Content-Type} header of the surrounding spec
     * is set to {@code multipart/form-data; boundary=<value>}, where
     * {@code <value>} is the textual form of the boundary.
     *
     * @param ps the parts to include in the body
     * @param br the boundary marker used to delimit the parts
     * @param hs the surrounding HTTP spec that will wrap the body
     */
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

    /**
     * Creates a multipart body from precomputed byte content.
     * <p>
     * This constructor is intended for cases where the full multipart
     * message has already been assembled, for example when reconstructing
     * a request from an existing payload.
     *
     * @param bs the raw bytes of the multipart message
     */
    public Multipart(final Bytes bs) {
        this.src = bs;
    }

    /**
     * Returns the raw bytes of this multipart HTTP message, including the
     * body and any headers added by the surrounding spec.
     *
     * @return the HTTP message as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the message
     */
    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
