package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;

/**
 * A complete HTTP request assembled from an {@link HttpSpec} and a final
 * terminator.
 * <p>
 * This class is the top-level entry point of the HTTP spec hierarchy: it
 * takes any {@link HttpSpec} — for example, a request line decorated with
 * headers and a body — and produces the full request bytes that can be sent
 * over the wire. The assembly consists of the spec's bytes followed by a
 * {@link Terminator}, which marks the end of the request and is required by
 * the HTTP protocol after the last header (or after the body, if one is
 * present).
 * <p>
 * Because the whole hierarchy is lazy, no bytes are produced until
 * {@link #bytes()} is called. At that point, the underlying spec is
 * materialized, a terminator is appended, and the resulting byte sequence
 * is returned. Any invariant violation raised by a nested spec — for
 * example, a missing header value or a malformed request line — propagates
 * out of this call.
 * <p>
 * A precomputed byte sequence can also be wrapped directly, which is useful
 * when reconstructing a request from an existing payload or when
 * implementing custom request shapes that do not fit the standard
 * decorator chain.
 */
public final class Request implements HttpSpec {

    /**
     * The underlying byte content of the request.
     */
    private final Bytes src;

    /**
     * Creates a request from the given HTTP spec by appending a final
     * terminator.
     * <p>
     * This constructor is the primary way to turn a fully assembled spec
     * (request line plus headers plus optional body) into a sendable HTTP
     * request. The terminator is appended after the spec's bytes so that
     * the resulting message is protocol-valid.
     *
     * @param origin the HTTP spec that provides the request content
     */
    public Request(final HttpSpec origin) {
        this(
            new Concat(
                new ByteView(origin),
                new Terminator()
            )
        );
    }

    /**
     * Creates a request from precomputed byte content.
     * <p>
     * This constructor is intended for cases where the full request bytes
     * have already been assembled, for example when reconstructing a
     * request from an existing payload or when implementing a custom
     * request shape.
     *
     * @param bs the raw bytes of the request
     */
    public Request(final Bytes bs) {
        this.src = bs;
    }

    /**
     * Returns the raw bytes of this HTTP request, including the final
     * terminator.
     *
     * @return the HTTP request as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the request
     */
    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
