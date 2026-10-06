package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * A {@link Bytes} view over the raw HTTP headers of an {@link HttpResponse}.
 * <p>
 * This class implements {@link Bytes} and adapts an HTTP response to the byte
 * abstraction by exposing its raw header block as a byte array. The header
 * block typically includes the status line and all response headers, exactly as
 * received over the socket, before anybody content.
 * <p>
 * This is useful for extracting and parsing header information (such as
 * {@code Content-Length}, {@code Transfer-Encoding}, {@code Set-Cookie}, etc.)
 * using the same textual and byte-level tools used elsewhere in the
 * application, without requiring a dedicated header-parsing API.
 */
public final class HeadersOf implements Bytes {

    /**
     * The HTTP response whose headers are exposed.
     */
    private final HttpResponse src;

    /**
     * Creates a new byte view over the headers of the given HTTP response.
     *
     * @param r the HTTP response whose headers are exposed
     */
    public HeadersOf(final HttpResponse r) {
        this.src = r;
    }

    /**
     * Returns the raw header block of the underlying HTTP response as a byte
     * array.
     * <p>
     * The bytes are obtained via {@link HttpResponse#headers()} and include the
     * status line and all response headers, exactly as received.
     *
     * @return the raw HTTP headers as a byte array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the headers
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.headers();
    }
}
