package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.charset.StandardCharsets;

/**
 * A {@link Bytes} decorator that extracts the payload (body) from a full HTTP
 * response.
 * <p>
 * An HTTP response consists of a header block, followed by a blank line
 * (represented by the sequence {@code \r\n\r\n}), and then the body. This class
 * takes a {@link Bytes} instance representing the full response and returns
 * only the body, discarding the headers and the blank-line separator.
 * <p>
 * The extraction is performed by decoding the raw bytes as ISO-8859-1 text
 * (which maps each byte to a single character without loss), locating the first
 * occurrence of the {@code \r\n\r\n} separator, and returning everything after
 * it. If the separator is not found, the entire content is treated as the body
 * and returned unchanged.
 * <p>
 * The use of ISO-8859-1 ensures that the byte-to-character mapping is
 * bijective, so that the extracted substring can be re-encoded to bytes without
 * corruption, even if the body contains binary data or characters outside the
 * ASCII range.
 * <p>
 * This class is used throughout the application to obtain the HTML content of
 * contest pages from the raw HTTP responses returned by the web driver.
 */
public final class PayloadOf implements Bytes {

    /**
     * The full HTTP response whose payload is extracted.
     */
    private final Bytes origin;

    /**
     * Creates a payload extractor over the given full HTTP response bytes.
     *
     * @param b the full HTTP response as raw bytes
     */
    public PayloadOf(final Bytes b) {
        this.origin = b;
    }

    /**
     * Returns the body (payload) of the underlying HTTP response.
     * <p>
     * The full response is decoded as ISO-8859-1 text, and the first occurrence
     * of the header/body separator {@code \r\n\r\n} is located. Everything
     * after that separator is returned as the payload. If the separator is not
     * present, the entire content is returned as the payload.
     *
     * @return the response body as a byte array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the full response bytes
     */
    @Override
    public byte[] content() throws InvariantViolation {
        final var full = new String(
            this.origin.content(),
            StandardCharsets.ISO_8859_1
        );
        final int idx = full.indexOf("\r\n\r\n");
        return (idx == -1 ? full : full.substring(idx + 4))
            .getBytes(StandardCharsets.ISO_8859_1);
    }
}
