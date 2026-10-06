package org.ejavdge.web.spec.header;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.NonEmpty;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;

/**
 * An {@link HttpSpec} decorator that appends one or more HTTP headers to an
 * existing HTTP message.
 * <p>
 * Given a header (or a collection of headers) and an underlying HTTP spec,
 * this class produces a new spec whose bytes are the concatenation of the
 * underlying spec's bytes followed by the header lines. Because each
 * {@link Header} already includes its own terminating line break, the
 * resulting byte sequence is a valid HTTP message with the additional
 * headers appended to the existing header block.
 * <p>
 * The underlying spec is required to be non-empty via {@link NonEmpty},
 * ensuring that a header is never emitted on its own without a preceding
 * request or status line. If the underlying spec is empty, an
 * {@link InvariantViolation} is thrown when the message is materialized.
 */
public final class WithHeaders implements HttpSpec {

    /**
     * The fully assembled HTTP message (underlying spec + headers).
     */
    private final Bytes src;

    /**
     * Creates a spec that appends a single header to the underlying HTTP
     * message.
     * <p>
     * This is a convenience constructor equivalent to passing a
     * single-element collection of headers to the more general
     * constructor.
     *
     * @param h the header to append
     * @param s the underlying HTTP spec
     */
    public WithHeaders(final Header h, final HttpSpec s) {
        this(new Items.Of<>(h), s);
    }

    /**
     * Creates a spec that appends the given collection of headers to the
     * underlying HTTP message.
     * <p>
     * The bytes of the underlying spec are materialized first and
     * required to be non-empty; the headers are then concatenated after
     * them in order, with each header already carrying its own
     * terminating line break.
     *
     * @param hs the headers to append, in order
     * @param s  the underlying HTTP spec
     */
    public WithHeaders(final Items<Header> hs, final HttpSpec s) {
        this.src = new Concat(
            new NonEmpty(
                new ByteView(s)
            ),
            new Concat(hs)
        );
    }

    /**
     * Returns the raw bytes of the assembled HTTP message, including the
     * underlying spec and the appended headers.
     *
     * @return the HTTP message as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the message, for example if the underlying
     *         spec is empty
     */
    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
